package top.upingou.manager.controller;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import top.upingou.pojo.TbGoods;
import top.upingou.pojo.TbItem;
import top.upingou.pojogroup.Goods;
import top.upingou.search.service.ItemSearchService;
import top.upingou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.UResult;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	@Reference
	private ItemSearchService itemSearchService;
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult<TbGoods>  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public UResult update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new UResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public UResult delete(Long [] ids){
		try {
			goodsService.delete(ids);
			// 同步删除索引
			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			return new UResult(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult<TbGoods> search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
	/**
	 * <p>Title: updateStatus</p>
	 * <p>Description: 批量修改商品的状态</p>
	 * <p>CreateDate:2019年3月10日 下午8:53:05</p>
	 * @param ids 商品的id集合
	 * @param status	商品的目标状态
	 * @return	返回自定义响应格式
	 */
	@RequestMapping("/updateStatus/{ids}/{status}")
	public UResult updateStatus(@PathVariable Long[] ids, @PathVariable String status) {
		try {
			goodsService.updateStatus(ids, status);
			
			// 按照SPU ID查询 SKU列表(状态为1)		
			if(status.equals("1")){//审核通过
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids, status);						
				// 调用搜索接口实现数据批量导入
				if(itemList.size()>0){				
					itemSearchService.importList(itemList);
				}else{
					System.out.println("没有明细数据");
				}
			}
			
			return new UResult(true, "审核成功");
		} catch (Exception e) {
			return new UResult(true, "审核失败");
		}
	}
	
}
