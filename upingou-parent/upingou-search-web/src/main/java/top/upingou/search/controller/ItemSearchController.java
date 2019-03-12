package top.upingou.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import top.upingou.search.service.ItemSearchService;

@RestController
@RequestMapping("itemSearch")
public class ItemSearchController {

	@Reference
	private ItemSearchService itemSearchService;
	
	@RequestMapping("/search")
	public Map<String, Object> search(@RequestBody Map searchMap ){
		return  itemSearchService.search(searchMap);
	}
}
