package top.upingou.pojogroup;

import java.io.Serializable;
import java.util.List;

import top.upingou.pojo.TbGoods;
import top.upingou.pojo.TbGoodsDesc;
import top.upingou.pojo.TbItem;

/**
* @author YiChao
* @version 创建时间：2019年3月5日 上午8:29:33
*    说明:组合商品实体类
*/
public class Goods implements Serializable{
	private TbGoods goods;	// 商品SPU
	private TbGoodsDesc goodsDesc;	// 商品SPU扩展描述
	private List<TbItem> itemList;	// 商品SKU列表
	public TbGoods getGoods() {
		return goods;
	}
	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}
	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public List<TbItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}
}
