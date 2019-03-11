package top.upingou.content.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import top.upingou.mapper.TbContentCategoryMapper;
import top.upingou.pojo.TbContentCategory;
import top.upingou.pojo.TbContentCategoryExample;
import top.upingou.pojo.TbContentCategoryExample.Criteria;
import top.upingou.content.service.ContentCategoryService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContentCategory> findAll() {
		return contentCategoryMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult<TbContentCategory> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContentCategory> page=   (Page<TbContentCategory>) contentCategoryMapper.selectByExample(null);
		return new PageResult<TbContentCategory>(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContentCategory contentCategory) {
		contentCategoryMapper.insert(contentCategory);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContentCategory contentCategory){
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContentCategory findOne(Long id){
		return contentCategoryMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			contentCategoryMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult<TbContentCategory> findPage(TbContentCategory contentCategory, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		
		if(contentCategory!=null){			
						if(contentCategory.getName()!=null && contentCategory.getName().length()>0){
				criteria.andNameLike("%"+contentCategory.getName()+"%");
			}
	
		}
		
		Page<TbContentCategory> page= (Page<TbContentCategory>)contentCategoryMapper.selectByExample(example);		
		return new PageResult<TbContentCategory>(page.getTotal(), page.getResult());
	}
	
}
