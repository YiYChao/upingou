package top.upingou.manager.controller;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import top.upingou.pojo.TbSeller;
import top.upingou.sellergoods.service.SellerService;

import entity.PageResult;
import entity.UResult;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeller> findAll(){			
		return sellerService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult<TbSeller>  findPage(int page,int rows){			
		return sellerService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public UResult add(@RequestBody TbSeller seller){
		try {
			sellerService.add(seller);
			return new UResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public UResult update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
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
	public TbSeller findOne(String id){
		return sellerService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public UResult delete(String [] ids){
		try {
			sellerService.delete(ids);
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
	public PageResult<TbSeller> search(@RequestBody TbSeller seller, int page, int rows  ){
		return sellerService.findPage(seller, page, rows);		
	}
	
	/**
	 * <p>Title: updateStatus</p>
	 * <p>Description: 更新商家状态</p>
	 * <p>CreateDate:2019年3月3日 上午9:59:58</p>
	 * @param sellerId	商家Id
	 * @param status	商家状态
	 * @return	返回自定义结构
	 */
	@RequestMapping("updateStatus/{sellerId}/{status}")
	public UResult updateStatus(@PathVariable String sellerId,@PathVariable String status) {
		try {
			sellerService.updateStatus(sellerId, status);
			return new UResult(true, "审核成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false, "审核失败");
		}
	}
}
