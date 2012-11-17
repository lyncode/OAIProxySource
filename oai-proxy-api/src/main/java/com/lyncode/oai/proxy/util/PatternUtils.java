package com.lyncode.oai.proxy.util;


public class PatternUtils {
	public static boolean validHttpURI (String uri) {
		String regex = "^http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		return uri.matches(regex);
	}
}
