package com.lyncode.oai.proxy.web.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.lyncode.oai.proxy.model.dao.api.UserDao;
import com.lyncode.oai.proxy.model.entity.User;

public class UserAuthenticationService implements UserDetailsService {
	@Autowired private UserDao dao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException {
	    User userEntity = dao.selectUserByEmail(arg0);
	    if (userEntity == null)
	      throw new UsernameNotFoundException("Password/Email incorrect");

	    return buildUserFromUserEntity(userEntity);
	}
	
	private static UserDetails buildUserFromUserEntity(User userEntity) {

		String username = userEntity.getEmail();
		String password = userEntity.getPassword();
		boolean enabled = userEntity.isActive();
		boolean accountNonExpired = userEntity.isActive();
		boolean credentialsNonExpired = userEntity.isActive();
		boolean accountNonLocked = userEntity.isActive();

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl("ROLE_MEMBER"));

		UserDetails user = new org.springframework.security.core.userdetails.User(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
		return user;
	}
}
