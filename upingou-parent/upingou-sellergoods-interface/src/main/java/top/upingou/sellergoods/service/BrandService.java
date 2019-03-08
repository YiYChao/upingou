package top.upingou.sellergoods.service;

import java.util.List;
import java.util.Map;

import entity.PageResult;
import entity.UResult;
import top.upingou.pojo.TbBrand;

/**
 * <p>Title: BrandService.java</p>
 * <p>Description: 品牌服务接口定义</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * @author YiChao
 * @date 2019年3月1日 下午4:20:33
 * @version 1.0.0
 */
public interface BrandService {

	/**
	 * <p>Title: findAll</p>
	 * <p>Description: 查找所有商品列表</p>
	 * <p>CreateDate:2019年3月1日 下午4:21:15</p>
	 * @return
	 */
	public List<TbBrand> findAll();
	
	/**
	 * <p>Title: findPage</p>
	 * <p>Description: 获得分页查询结果</p>
	 * <p>CreateDate:2019年3月1日 下午4:21:49</p>
	 * @param pageNum	当前页数
	 * @param pageSize	每页记录数
	 * @return	自定义结构，包含当前页记录和总记录数
	 */
	public PageResult<TbBrand> findPage(int pageNum, int pageSize);
	
	/**
	 * <p>Title: add</p>
	 * <p>Description: 新增品牌记录</p>
	 * <p>CreateDate:2019年3月1日 下午5:34:54</p>
	 * @param brand	将要新增的品牌
	 */
	public void add(TbBrand brand);
	
	/**
	 * <p>Title: findBrandById</p>
	 * <p>Description: 通过Id查询品牌</p>
	 * <p>CreateDate:2019年3月1日 下午6:20:15</p>
	 * @param id	品牌Id
	 * @return	返回品牌信息
	 */
	public TbBrand findBrandById(Long id);
	
	/**
	 * <p>Title: update</p>
	 * <p>Description: 更新品牌信息</p>
	 * <p>CreateDate:2019年3月1日 下午6:21:16</p>
	 * @param brand 更新后的品牌信息
	 */
	public void update(TbBrand brand);
	
	/**
	 * <p>Title: delete</p>
	 * <p>Description: 删除商品集</p>
	 * <p>CreateDate:2019年3月1日 下午7:37:53</p>
	 * @param ids	品牌id集合
	 */
	public void delete(Long[] ids);
	
	/**
	 * <p>Title: findPage</p>
	 * <p>Description: 模糊查询品牌</p>
	 * <p>CreateDate:2019年3月1日 下午8:05:12</p>
	 * @param brand	需要查询匹配的信息
	 * @param pageNum	当前页
	 * @param pageSize	每页记录数
	 * @return	自定义结构，包含当前页记录和总记录数
	 */
	public PageResult<TbBrand> findPage(TbBrand brand, int pageNum, int pageSize);
	
	/**
	 * <p>Title: selectOptionList</p>
	 * <p>Description: 查询品牌的Id和名称</p>
	 * <p>CreateDate:2019年3月2日 下午3:14:16</p>
	 * @return	返回Map集合
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectOptionList();
}
