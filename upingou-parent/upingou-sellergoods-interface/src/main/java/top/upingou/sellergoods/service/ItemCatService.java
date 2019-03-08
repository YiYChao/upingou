package top.upingou.sellergoods.service;
import java.util.List;
import top.upingou.pojo.TbItemCat;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ItemCatService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbItemCat> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult<TbItemCat> findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbItemCat itemCat);
	
	
	/**
	 * 修改
	 */
	public void update(TbItemCat itemCat);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbItemCat findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult<TbItemCat> findPage(TbItemCat itemCat, int pageNum,int pageSize);
	
	/**
s	 * <p>Title: findByParentId</p>
	 * <p>Description: 通过上级Id返回子类列表</p>
	 * <p>CreateDate:2019年3月4日 下午3:21:39</p>
	 * @param parentId	上级Id
	 * @return	分类列表
	 */
	public List<TbItemCat> findByParentId(Long parentId);
}
