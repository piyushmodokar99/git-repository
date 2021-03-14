package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.security.model.AuthRequest;
import com.security.model.AuthResponse;
import com.security.service.MyUserDetailsService;
import com.security.util.JWTUtil;

@RestController
public class LoginController 
{
	@Autowired
	private AuthenticationManager authenticationManager;  
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	JWTUtil jWTUtil;
	
	@RequestMapping({"/hello"})
	public String hello()
	{
		return "Welcome Piyush";
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) throws Exception
	{
		AuthResponse authResponse = null;
		
		try 
		{
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), 
							authRequest.getPassword()));
		} 
		catch (BadCredentialsException e) 
		{
			e.printStackTrace();
			throw new Exception("Username or Pass is incorrect", e);
		}
		
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUsername());
		
		String jwt = jWTUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthResponse(jwt));
	}
}
