package top.upingou.manager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author YiChao
* @version 创建时间：2019年3月2日 下午7:59:03
* 说明: 用户登录
*/
@RestController
@RequestMapping("/login")
public class LoginController {

	/**
	 * <p>Title: showName</p>
	 * <p>Description: 获得当前登录的用户名</p>
	 * <p>CreateDate:2019年3月2日 下午8:03:17</p>
	 * @return 返回登录的用户名
	 */
	@RequestMapping("/name")
	public Map<String, String> showName(){
		Map<String, String> map = new HashMap<String, String>();
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		map.put("loginName", name);
		return map;
	}
}
