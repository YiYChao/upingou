package top.upingou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;

import top.upingou.pojo.TbItem;
import top.upingou.search.service.ItemSearchService;

/**
 * 商品搜索接口实现
 * @author YiChao
 *
 */
@Service(timeout=10000)
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private SolrTemplate solrTemplate;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public Map<String, Object> search(Map searchMap) {
		Map<String,Object> map=new HashMap<>();
		
		// 1.查询列表
		map.putAll(searchList(searchMap));
		// 2.根据从关键字查询商品分类
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		// 查询品牌和规格列表
		if(categoryList.size() > 0) {
			map.putAll(searchBrandAndSpecList(categoryList.get(0)));
		}
		return map;
	}
	
	/**
	 * 根据关键字搜索列表
	 * @param searchMap
	 * @return
	 */
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
		
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query , TbItem.class);
		for(HighlightEntry<TbItem> entry : highlightPage.getHighlighted()) {
			TbItem item = entry.getEntity(); 	// 获得原实体类
			if(entry.getHighlights().size() > 0 && entry.getHighlights().get(0).getSnipplets().size() > 0) {
				item.setTitle(entry.getHighlights().get(0).getSnipplets().get(0)); 	// 设置高亮结果
			}
		}
		map.put("rows", highlightPage.getContent());
		return map;
	}
	
	/**
	 * 查询分类列表
	 * @param searchMap
	 * @return
	 */
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
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);	// 获取模板Id
		if(typeId != null) {
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);	// 根据模板ID查询品牌列表
			map.putIfAbsent("brandList", brandList);
			
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);	// 根据模板ID查询规格列表
			map.putIfAbsent("specList", specList);
		}
		return map;
	}
}
