package com.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.jwt.business.UserService;
import com.jwt.util.JWTUtil;
import com.jwt.vo.JwtAuthenticationToken;
import com.jwt.vo.User;

public class MyProvider implements AuthenticationProvider {

	@Autowired
	UserService userService;
	
	@Autowired
	JWTUtil jWTUtil;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if(authentication instanceof JwtAuthenticationToken){
    		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken)authentication;
    		try {
				User user = jWTUtil.parseToken(jwtAuthenticationToken.getToken());
			} catch (Exception e) {
				e.printStackTrace();
			}

    		UsernamePasswordAuthenticationToken successFullAuthentication = new UsernamePasswordAuthenticationToken(jwtAuthenticationToken.getPrincipal(), jwtAuthenticationToken.getCredentials(),jwtAuthenticationToken.getAuthorities());
    		return successFullAuthentication;
    	}else {
    		
    		
    		return null;
    	}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
