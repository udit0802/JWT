package com.jwt.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.auth.HRMSAuthentication;
import com.jwt.business.UserService;
import com.jwt.exception.ApplicationException;
import com.jwt.util.JWTUtil;
import com.jwt.vo.ResponseWrapper;
import com.jwt.vo.Status;
import com.jwt.vo.User;

@RestController
@RequestMapping("/api")
public class UserControllerAPI {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HRMSAuthentication myAuthProvider;
	
	@Autowired
	JWTUtil jWTUtil;

	@CrossOrigin
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public ResponseWrapper<String> login(@RequestBody User user) throws ApplicationException{
		try {
			//check if that user exists in database
			
			User userdetails = userService.getUserByOlmId(user.getOlmId());
			List<String> roles = Arrays.asList(userdetails.getRole().split("\\s*,\\s*"));
			
			if(roles.contains("PATROLLER") || roles.contains("NMT")){
				
				myAuthProvider.autheticateUser(user.getOlmId(), user.getPassword());
				
			}else{
				throw new ApplicationException(new Status(500, "User is not authorized as StoreManager"), "User not authorized as Store Manager !!");
			}
			
		} catch (BadCredentialsException e) {
			// TODO Auto-generated catch block
			ResponseWrapper.getFailureResponse(null, new Status(500, "OlmId or password is incorrect"));
			
		}
		return ResponseWrapper.getSuccessResponse(jWTUtil.generateToken(user));
	}
	
	@RequestMapping(value = "/patroller/getDetails",method = RequestMethod.GET)
	public User getPatrollerDetails(Authentication auth,@RequestParam(value = "username")String username){
		return userService.getUserByOlmId(username);
	}
}
