package top.upingou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import top.upingou.mapper.TbSpecificationOptionMapper;
import top.upingou.mapper.TbTypeTemplateMapper;
import top.upingou.pojo.TbSpecificationOption;
import top.upingou.pojo.TbSpecificationOptionExample;
import top.upingou.pojo.TbTypeTemplate;
import top.upingou.pojo.TbTypeTemplateExample;
import top.upingou.pojo.TbTypeTemplateExample.Criteria;
import top.upingou.sellergoods.service.TypeTemplateService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult<TbTypeTemplate> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbTypeTemplate> page=   (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult<TbTypeTemplate>(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult<TbTypeTemplate> findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
		
		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);		
		
		save2Redis(); // 将数据存入缓存
		
		return new PageResult<TbTypeTemplate>(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map<Object, Object>> selectOptionList() {
		return typeTemplateMapper.selectOptionList();
	}
	
	@Override
	public Map<Object, Object> findOptionById(Long id){
		return typeTemplateMapper.findOptionById(id);
	}

	@SuppressWarnings({"rawtypes", "unchecked" })
	@Override
	public List<Map> findSpecList(Long id) {
		TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
		List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
		for (Map map : list) {
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			top.upingou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(new Long((Integer) map.get("id")));
			List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
			map.put("options", options);
		}
		return list;
	}
	
	/**
	 * 将数据放入缓存
	 */
	private void save2Redis() {
		List<TbTypeTemplate> list = findAll();	// 获得模板数据
		for (TbTypeTemplate tbTypeTemplate : list) {
			List<Map> brandList = JSON.parseArray(tbTypeTemplate.getBrandIds(), Map.class);	// 存储品牌列表
			redisTemplate.boundHashOps("brandList").put(tbTypeTemplate.getId(), brandList);
			
			// 存储规格列表
			List<Map> specList = findSpecList(tbTypeTemplate.getId()); 	// //根据模板ID查询规格列表
			redisTemplate.boundHashOps("specList").put(tbTypeTemplate.getId(), specList);
			
		}
		System.out.println("品牌规格放入缓存");
	}
}
