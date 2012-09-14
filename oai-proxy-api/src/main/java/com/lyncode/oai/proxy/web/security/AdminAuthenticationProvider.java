package com.lyncode.oai.proxy.web.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.lyncode.oai.proxy.core.ConfigurationManager;

public class AdminAuthenticationProvider implements AuthenticationProvider {
	static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	static {
		AUTHORITIES.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
	}
	
	
	@Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
		Configuration config = ConfigurationManager.getConfiguration();
		
		String principal = (String) auth.getPrincipal();
		String password = (String) auth.getCredentials();
		
		if (principal.equals(config.getString("admin.user")) && password.equals(config.getString("admin.pass")))
			return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), AUTHORITIES);
		else
	        throw new BadCredentialsException("Username/Password does not match for " + principal);
    }

    @Override
    public boolean supports(Class<? extends Object> paramClass) {
        return true;
    }
}