package com.hydev.security1.config.oauth.provider;

import java.util.Map;

public class FacebookUserInfo2 implements OAuth2UserInfo{
	
	private Map<String,Object> attributes;
	
	public FacebookUserInfo2(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		return (String)attributes.get("id");
	}

	@Override
	public String getProvider() {
		return "facebook";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}
	
}
