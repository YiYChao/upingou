package top.upingou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import entity.PageResult;
import entity.UResult;
import top.upingou.pojo.TbBrand;
import top.upingou.sellergoods.service.BrandService;

/**
 * <p>Title: BrandController.java</p>
 * <p>Description: 品牌服务控制层</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * @author YiChao
 * @date 2019年3月1日 下午4:26:23
 * @version 1.0.0
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;
	
	/**
	 * <p>Title: findAll</p>
	 * <p>Description: 返回所有品牌记录</p>
	 * <p>CreateDate:2019年3月1日 下午4:30:37</p>
	 * @return 返回所有品牌记录
	 */
	@RequestMapping("/list")
	public List<TbBrand> findAll(){
		
		List<TbBrand> brandList = brandService.findAll();
		return brandList;
	}
	
	/**
	 * <p>Title: findPage</p>
	 * <p>Description: 分页查询</p>
	 * <p>CreateDate:2019年3月1日 下午4:33:46</p>
	 * @param page	当前页
	 * @param rows	每页显示记录数
	 * @return	返回自定义分页结构
	 */
	@RequestMapping("/pageList/{page}/{rows}")
	public PageResult<TbBrand> findPage(@PathVariable Integer page, @PathVariable Integer rows) {
		
		return brandService.findPage(page, rows);
	}
	
	/**
	 * <p>Title: add</p>
	 * <p>Description: 新增品牌</p>
	 * <p>CreateDate:2019年3月1日 下午6:03:38</p>
	 * @param brand	新增品牌
	 * @return	自定义相应结构
	 */
	@RequestMapping("/add")
	public UResult add(@RequestBody TbBrand brand) {
		 try {
			 brandService.add(brand);
			 return new UResult(true, "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false, "新增失败");
		}
	}
	
	/**
	 * <p>Title: findBrandById</p>
	 * <p>Description: 通过Id查询品牌记录</p>
	 * <p>CreateDate:2019年3月1日 下午6:29:31</p>
	 * @param id	品牌Id
	 * @return	返回品牌记录
	 */
	@RequestMapping("/findById/{id}")
	public TbBrand findBrandById(@PathVariable Long id) {
		return brandService.findBrandById(id);
	}
	
	/**
	 * <p>Title: update</p>
	 * <p>Description: 更新品牌信息</p>
	 * <p>CreateDate:2019年3月1日 下午6:30:58</p>
	 * @param brand	新的品牌信息
	 * @return	自定义相应结构
	 */
	@RequestMapping("/update")
	public UResult update(@RequestBody TbBrand brand) {
		try {
			brandService.update(brand);
			return new UResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false, "修改失败");
		}
	}
	
	@RequestMapping("/delete")
	public UResult delete(@RequestBody Long[] ids) {
		try {
			brandService.delete(ids);
			return new UResult(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new UResult(false, "删除失败");
		}
	}
	
	@RequestMapping("/search/{pageNum}/{pageSize}")
	public PageResult<TbBrand> search(@RequestBody TbBrand brand,@PathVariable int pageNum,@PathVariable int pageSize){
		return brandService.findPage(brand, pageNum, pageSize);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}
}
