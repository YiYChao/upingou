package top.upingou.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import top.upingou.pojo.TbSeller;
import top.upingou.sellergoods.service.SellerService;

/**
* @author YiChao
* @version 创建时间：2019年3月4日 上午9:34:50
* 说明: Spring Security 认证类
*/
public class UserDetailsServiceLmpl implements UserDetailsService{

	private SellerService sellerService;	// 远程调用商家服务接口，需要配合Dubbo使用
	
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();;
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		// 获得商家对象
		TbSeller seller = sellerService.findOne(username);
		if(seller != null) {
			if("1".equals(seller.getStatus())) {
				return new User(username,seller.getPassword(),grantedAuths );
			}
		}
		return null;
	}
}
