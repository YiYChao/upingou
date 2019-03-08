package top.upingou.sellergoods.service;
import java.util.List;
import java.util.Map;

import top.upingou.pojo.TbSpecification;
import top.upingou.pojogroup.Specification;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SpecificationService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSpecification> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult<TbSpecification> findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Specification specification);
	
	
	/**
	 * 修改
	 */
	public void update(Specification specification);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Specification findOne(Long id);
	
	
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
	public PageResult<TbSpecification> findPage(TbSpecification specification, int pageNum,int pageSize);

	/**
	 * <p>Title: selectOptionList</p>
	 * <p>Description: 查询规格id和名称</p>
	 * <p>CreateDate:2019年3月2日 下午3:32:59</p>
	 * @return	返回Map列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectOptionList();
	
}
