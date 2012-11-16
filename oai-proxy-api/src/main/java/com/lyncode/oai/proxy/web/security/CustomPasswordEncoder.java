package com.lyncode.oai.proxy.web.security;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.lyncode.oai.proxy.util.CryptoUtils;

public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String rawPass, Object salt) {
		return CryptoUtils.sha1(rawPass);
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		return encPass.equals(this.encodePassword(rawPass, salt));
	}

}
