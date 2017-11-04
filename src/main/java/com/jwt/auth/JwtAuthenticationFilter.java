package com.jwt.auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.jwt.exception.ApplicationException;
import com.jwt.util.JWTUtil;
import com.jwt.vo.JwtAuthenticationToken;
import com.jwt.vo.User;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	JWTUtil jWTUtil;
	
	public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new org.springframework.ldap.AuthenticationException();
        }

        String authToken = header.substring(7);

        User parsedUser = null;
		try {
			parsedUser = jWTUtil.parseToken(authToken);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String[] roles = parsedUser.getRole().split(",");
        for(int i=0;i<roles.length;i++){
        	roles[i] = "ROLE_"+roles[i].toUpperCase();
        }
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roles);
        
        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(parsedUser.getOlmId(),
        		authToken,authorityList);

        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }

}
