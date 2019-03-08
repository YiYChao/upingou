package top.upingou.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author YiChao
* @version 创建时间：2019年3月4日 下午12:18:45
* 说明: 登录前端控制器
*/
@RestController
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("/name")
	public Map<String, String> showName(){
		Map<String, String> map = new HashMap<String, String>();
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		map.put("name", name);
		return map;
	}
}
