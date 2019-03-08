package top.upingou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import top.upingou.mapper.TbSpecificationMapper;
import top.upingou.mapper.TbSpecificationOptionMapper;
import top.upingou.pojo.TbSpecification;
import top.upingou.pojo.TbSpecificationExample;
import top.upingou.pojo.TbSpecificationExample.Criteria;
import top.upingou.pojo.TbSpecificationOption;
import top.upingou.pojo.TbSpecificationOptionExample;
import top.upingou.pojogroup.Specification;
import top.upingou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult<TbSpecification> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult<TbSpecification>(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		TbSpecification tbSpecification = specification.getSpecification();
		specificationMapper.insert(tbSpecification);
		
		List<TbSpecificationOption> optionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption option : optionList) {
			option.setSpecId(tbSpecification.getId());
			specificationOptionMapper.insert(option);
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		specificationMapper.updateByPrimaryKey(specification.getSpecification());
		
		TbSpecificationOptionExample optionExample = new TbSpecificationOptionExample();
		top.upingou.pojo.TbSpecificationOptionExample.Criteria criteria = optionExample.createCriteria();
		criteria.andSpecIdEqualTo(specification.getSpecification().getId());
		// 删除原来的规格记录
		specificationOptionMapper.deleteByExample(optionExample);
		// 循环增加新记录
		List<TbSpecificationOption> optionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption option : optionList) {
			option.setSpecId(specification.getSpecification().getId());
			specificationOptionMapper.insert(option);
		}
		
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		Specification specification = new Specification();
		specification.setSpecification(specificationMapper.selectByPrimaryKey(id));
		
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		top.upingou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();	
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> optionList = specificationOptionMapper.selectByExample(example);
		specification.setSpecificationOptionList(optionList);
		
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			// 删除原有的规格名称
			specificationMapper.deleteByPrimaryKey(id);
			// 删除原有的规格选项
			TbSpecificationOptionExample optionExample = new TbSpecificationOptionExample();
			top.upingou.pojo.TbSpecificationOptionExample.Criteria criteria = optionExample.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(optionExample);
		}		
	}
	
	
		@Override
	public PageResult<TbSpecification> findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult<TbSpecification>(page.getTotal(), page.getResult());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> selectOptionList() {
		
		return specificationMapper.selectOptionList();
	}
	
}
