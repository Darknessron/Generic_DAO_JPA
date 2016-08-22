package com.rontseng.dao.support;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date 2016-Aug-22 1:27:40 PM
 */
public abstract class AbstractResultVO {
	private String token;
	private String audience;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}
}
