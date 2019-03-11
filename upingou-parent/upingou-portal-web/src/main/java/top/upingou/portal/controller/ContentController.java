package top.upingou.portal.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import top.upingou.content.service.ContentService;
import top.upingou.pojo.TbContent;

@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;
	
	/**
	 * 通过广告分类Id查询广告列表
	 * @param categoryId	广告分类Id
	 * @return	返回JSON格式的列表
	 */
	@RequestMapping("/findByCategoryId/{categoryId}")
	public List<TbContent> findByCategoryId(@PathVariable Long categoryId){
		return contentService.findByCategoryId(categoryId);
	}
}
