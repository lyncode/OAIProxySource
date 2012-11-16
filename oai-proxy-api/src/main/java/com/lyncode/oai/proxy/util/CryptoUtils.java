package com.lyncode.oai.proxy.util;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class CryptoUtils {
	public static String sha1 (String password)
	{
		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
	    return encoder.encodePassword(password, "");
	}
}
