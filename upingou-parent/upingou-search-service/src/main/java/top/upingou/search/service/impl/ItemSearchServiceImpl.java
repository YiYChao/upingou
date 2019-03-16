package top.upingou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import top.upingou.pojo.TbItem;
import top.upingou.search.service.ItemSearchService;

/**
 * 商品搜索接口实现
 * @author YiChao
 *
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private SolrTemplate solrTemplate;
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> search(Map searchMap) {
		Map<String,Object> map=new HashMap<>();
		
		//关键字空格处理 
		String keywords = (String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));

		// 1.查询列表
		map.putAll(searchList(searchMap));
		// 2.根据从关键字查询商品分类
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		// 3.查询品牌和规格列表
		String categoryName = (String)searchMap.get("category");
		if(!"".equals(categoryName)){	// 如果有分类名称
			map.putAll(searchBrandAndSpecList(categoryName));			
		}else{	// 如果没有分类名称，按照第一个查询
			if(categoryList.size()>0){
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}
		return map;
	}
	
	/**
	 * 根据关键字搜索列表
	 * @param searchMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map searchList(Map searchMap) {
		Map map = new HashMap();
		
		// 高亮显示查询
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions options = new HighlightOptions().addField("item_title");
		options.setSimplePrefix("<em style='color:red'>");	// 设置高亮前缀
		options.setSimplePostfix("</em>");	// 设置高亮后缀
		query.setHighlightOptions(options);	// 加入高亮选项
		
		// 关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);	// 添加 关键字查询
		
		// 按分类筛选
		if(!"".equals(searchMap.get("category"))){			
			Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
			FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		// 按品牌筛选
		if(!"".equals(searchMap.get("brand"))){			
			Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
			FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		// 过滤规格
		if(searchMap.get("spec")!=null){
				Map<String,String> specMap= (Map) searchMap.get("spec");
				for(String key:specMap.keySet() ){
					Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
					FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
					query.addFilterQuery(filterQuery);				
				}			
		}

		// 按价格筛选
		if(!"".equals(searchMap.get("price"))){
			String[] price = ((String) searchMap.get("price")).split("-");
			if(!price[0].equals("0")){	// 如果区间起点不等于0
				Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
				FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);				
			}		
			if(!price[1].equals("*")){	// 如果区间终点不等于*
				Criteria filterCriteria=new  Criteria("item_price").lessThanEqual(price[1]);
				FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);				
			}
		}

		// 分页查询		
		Integer pageNo= (Integer) searchMap.get("pageNo");	// 提取页码
		if(pageNo==null){
			pageNo=1;	// 默认第一页
		}
		Integer pageSize=(Integer) searchMap.get("pageSize");	// 每页记录数 
		if(pageSize==null){
			pageSize=20;	// 默认20
		}
		query.setOffset((pageNo-1)*pageSize);	// 从第几条记录查询
		query.setRows(pageSize);

		// 排序
		String sortValue= (String) searchMap.get("sort");//ASC  DESC  
		String sortField= (String) searchMap.get("sortField");//排序字段
		if(sortValue!=null && !sortValue.equals("")){  
			if(sortValue.equals("ASC")){
				Sort sort=new Sort(Sort.Direction.ASC, "item_"+sortField);
				query.addSort(sort);
			}
			if(sortValue.equals("DESC")){		
				Sort sort=new Sort(Sort.Direction.DESC, "item_"+sortField);
				query.addSort(sort);
			}			
		}
		
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query , TbItem.class);
		for(HighlightEntry<TbItem> entry : highlightPage.getHighlighted()) {
			TbItem item = entry.getEntity(); 	// 获得原实体类
			if(entry.getHighlights().size() > 0 && entry.getHighlights().get(0).getSnipplets().size() > 0) {
				item.setTitle(entry.getHighlights().get(0).getSnipplets().get(0)); 	// 设置高亮结果
			}
		}
		map.put("rows", highlightPage.getContent());
		map.put("totalPages", highlightPage.getTotalPages());	// 返回总页数
		map.put("total", highlightPage.getTotalElements());	// 返回总记录数
		return map;
	}
	
	/**
	 * 查询分类列表
	 * @param searchMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<String> searchCategoryList(Map searchMap) {
		List<String> list = new ArrayList<String>();
		
		Query query = new SimpleHighlightQuery();
		// 按照关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);	// 添加 关键字查询

		// 设置分组选项
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);	// 添加分组查询
		
		// 得到分组页
		GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query , TbItem.class);
		GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");	// 根据列得到分组结果集
		Page<GroupEntry<TbItem>> entries = groupResult.getGroupEntries();	// 得到分组结果入口页
		List<GroupEntry<TbItem>> content = entries.getContent(); 	// 得到分组入口集合
		for(GroupEntry<TbItem> entry: content) {
			list.add(entry.getGroupValue());	// 将分组结果的名称封装到返回值中
		}
		return list;
	}
	
	/**
	 * 从缓存查询品牌和规格列表
	 * @param category	分类名称
	 * @return	返回封装的Map对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map searchBrandAndSpecList(String category) {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
       
		Map map = new HashMap();
		String typeId = redisTemplate.boundHashOps("itemCat").get(category).toString();	// 获取模板Id
		if(typeId != null) {
			List brandList = JSON.parseArray(redisTemplate.boundHashOps("brandList").get(typeId).toString(),Map.class);	// 根据模板ID查询品牌列表
			map.putIfAbsent("brandList", brandList);
			
			List specList = JSON.parseArray(redisTemplate.boundHashOps("specList").get(typeId).toString(),Map.class);	// 根据模板ID查询规格列表
			map.putIfAbsent("specList", specList);
		}
		return map;
	}

	@Override
	public void importList(List list) {
		solrTemplate.saveBeans(list);	
		solrTemplate.commit();
	}

	@Override
	public void deleteByGoodsIds(List goodsIdList) {
		Query query=new SimpleQuery();		
		Criteria criteria=new Criteria("item_goodsid").in(goodsIdList);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
}
