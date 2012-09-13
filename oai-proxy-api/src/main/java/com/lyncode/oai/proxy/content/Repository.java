package com.lyncode.oai.proxy.content;

public class Repository {
	private String baseUrl;
	private String set;

	public Repository(String baseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.set = null;
	}

	public Repository(String baseUrl, String set) {
		super();
		this.baseUrl = baseUrl;
		this.set = set;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getSet() {
		return set;
	}
}
