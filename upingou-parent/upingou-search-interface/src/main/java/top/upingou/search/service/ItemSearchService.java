package top.upingou.search.service;

import java.util.List;
import java.util.Map;

/**
 * 商品搜索接口定义
 * @author YiChao
 *
 */
public interface ItemSearchService {

	/**
	 * 搜索方法
	 * @param searchMap	封装的搜索参数
	 * @return	返回封装结果
	 */
	public Map<String, Object> search(Map searchMap);
	
	/**
	 * 导入商品数据
	 * @param list
	 */
	public void importList(List list);
	
	/**
	 * 根据商品id(SPU)列表删除索引
	 * @param goodsIdList
	 */
	public void deleteByGoodsIds(List goodsIdList);
}
