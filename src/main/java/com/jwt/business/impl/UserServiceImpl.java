package com.jwt.business.impl;

import com.jwt.business.UserService;
import com.jwt.vo.User;

public class UserServiceImpl implements UserService{
	

	@Override
	public User getUserByOlmId(String username) {
		User u = new User();
		u.setOlmId(username);
		if("B0096703".equals(username.toUpperCase())){
			u.setRole("PATROLLER");
		}else if("B0096700".equals(username.toUpperCase())){
			u.setRole("NMT");
		}else{
			u.setRole("PATROLLER,NMT");
		}
		return u;
		
	}
	
}
