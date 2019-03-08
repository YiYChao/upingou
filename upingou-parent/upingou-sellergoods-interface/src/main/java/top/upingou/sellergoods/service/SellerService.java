package top.upingou.sellergoods.service;
import java.util.List;
import top.upingou.pojo.TbSeller;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSeller> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult<TbSeller> findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeller seller);
	
	
	/**
	 * 修改
	 */
	public void update(TbSeller seller);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSeller findOne(String id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(String [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult<TbSeller> findPage(TbSeller seller, int pageNum,int pageSize);
	
	/**
	 * <p>Title: updateStatus</p>
	 * <p>Description: 更新商家状态</p>
	 * <p>CreateDate:2019年3月3日 上午9:53:49</p>
	 * @param sellerId	商家Id
	 * @param status	商家状态
	 */
	public void updateStatus(String sellerId,String status);
}
