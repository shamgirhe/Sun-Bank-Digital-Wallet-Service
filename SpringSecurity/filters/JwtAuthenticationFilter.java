package com.sunBank.security.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunBank.security.repositories.AppUserRepository;
import com.sunBank.security.utilities.JWTUtil;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	
	private AppUserRepository appUserRepository;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager,AppUserRepository appUserRepository)
	{
		this.authenticationManager = authenticationManager;
		this.appUserRepository = appUserRepository;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials","true");
		response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
		response.setHeader("Access-Control-Max-Age","3600");
		response.setHeader("Access-Control-Allow-Headers","Content-Type, Accept,X-Requested-With,remember-me");
		String username = null;
		String password = null;
		
		try
		{
			Map<String,String> requestMap = new ObjectMapper().readValue(request.getInputStream(),Map.class);
			username = requestMap.get("username");
			password = requestMap.get("password");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User)authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET); // here we pass algorithm to secret key 
		
		String jwtAccessToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JWTUtil.EXPIRE_ACCESS_TOKEN))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);
		
		String jwtRefreshToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JWTUtil.EXPIRE_REFRESH_TOKEN))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);
		
		Map<String,String> idToken = new HashMap<>();
		idToken.put("jwt",jwtAccessToken);
		idToken.put("refreshToken", jwtRefreshToken);
		
		idToken.put("roles",String.join(",",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
		
		if(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).toString().contains("CUSTOMER"))
		{
			idToken.put("id", String.valueOf(1));
		}
		
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), idToken);
	}

}
