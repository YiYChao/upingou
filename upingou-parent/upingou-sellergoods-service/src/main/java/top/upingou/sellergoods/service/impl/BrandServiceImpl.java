package top.upingou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;
import top.upingou.mapper.TbBrandMapper;
import top.upingou.pojo.TbBrand;
import top.upingou.pojo.TbBrandExample;
import top.upingou.pojo.TbBrandExample.Criteria;
import top.upingou.sellergoods.service.BrandService;

/**
 * <p>Title: BrandServiceImpl.java</p>
 * <p>Description: 品牌服务接口实现</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * @author YiChao
 * @date 2019年3月1日 下午4:23:46
 * @version 1.0.0
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService{

	@Autowired
	private TbBrandMapper brandMapper;
	
	@Override
	public List<TbBrand> findAll() {
		
		return brandMapper.selectByExample(null);
	}

	@Override
	public PageResult<TbBrand> findPage(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
		return new PageResult<TbBrand>(page.getTotal(), page.getResult());
	}

	@Override
	public void add(TbBrand brand) {
		
		brandMapper.insert(brand);
	}

	@Override
	public TbBrand findBrandById(Long id) {
		
		TbBrand brand = brandMapper.selectByPrimaryKey(id);
		return brand;
	}

	@Override
	public void update(TbBrand brand) {
		
		brandMapper.updateByPrimaryKey(brand);
	}

	@Override
	public void delete(Long[] ids) {
		
		for (Long id : ids) {
			brandMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult<TbBrand> findPage(TbBrand brand, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		TbBrandExample example = new TbBrandExample();
		Criteria criteria = example.createCriteria();	// 创建查询条件
		if(brand != null) {
			if(brand.getName() != null && brand.getName().length() > 0) {
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if(brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
				criteria.andFirstCharEqualTo(brand.getFirstChar());
			}
		}
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
		return new PageResult<TbBrand>(page.getTotal(), page.getResult());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> selectOptionList() {
		
		return brandMapper.selectOptionList();
	}
	
}
