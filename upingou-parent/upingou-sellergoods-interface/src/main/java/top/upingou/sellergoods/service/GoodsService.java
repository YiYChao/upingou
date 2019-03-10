package top.upingou.sellergoods.service;
import java.util.List;
import top.upingou.pojo.TbGoods;
import top.upingou.pojogroup.Goods;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult<TbGoods> findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
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
	public PageResult<TbGoods> findPage(TbGoods goods, int pageNum,int pageSize);

	/**
	 * <p>Title: updateStatus</p>
	 * <p>Description: 批量修改商品的状态</p>
	 * <p>CreateDate:2019年3月10日 下午8:46:18</p>
	 * @param ids	商品id列表
	 * @param status 将要修改的状态值
	 */
	public void updateStatus(Long ids[], String status);	
}
