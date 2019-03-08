package top.upingou.sellergoods.service;
import java.util.List;
import java.util.Map;

import top.upingou.pojo.TbTypeTemplate;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface TypeTemplateService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbTypeTemplate> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult<TbTypeTemplate> findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbTypeTemplate typeTemplate);
	
	
	/**
	 * 修改
	 */
	public void update(TbTypeTemplate typeTemplate);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbTypeTemplate findOne(Long id);
	
	
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
	public PageResult<TbTypeTemplate> findPage(TbTypeTemplate typeTemplate, int pageNum,int pageSize);
	
	/**
	 * <p>Title: selectOptionList</p>
	 * <p>Description: 获得模板的id和名称</p>
	 * <p>CreateDate:2019年3月4日 下午4:37:34</p>
	 * @return	返回Map列表
	 */
	public List<Map<Object, Object>> selectOptionList();

	/**
	 * <p>Title: findOptionById</p>
	 * <p>Description: 通过模板id查询已有选项</p>
	 * <p>CreateDate:2019年3月4日 下午5:30:22</p>
	 * @param id	模板id
	 * @return	Map集合
	 */
	Map<Object, Object> findOptionById(Long id);
	
	/**
	 * <p>Title: findSpecList</p>
	 * <p>Description: 查找模板总规格选项</p>
	 * <p>CreateDate:2019年3月6日 上午10:05:10</p>
	 * @param id	模板Id
	 * @return	返回模板的Map列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> findSpecList(Long id);
}
