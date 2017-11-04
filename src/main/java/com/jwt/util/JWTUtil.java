package com.jwt.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.jwt.exception.ApplicationException;
import com.jwt.vo.Status;
import com.jwt.vo.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtil {

	@Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.valid.duration}")
    private Long validDuration;
    
    public User parseToken(String token) throws ApplicationException {
            try {
				Claims body = Jwts.parser()
				        .setSigningKey(secret)
				        .parseClaimsJws(token)
				        .getBody();
				if(body.getExpiration().getTime() <= new Date().getTime()){
					throw new ApplicationException(new Status(401, "Session Expired!"));
				}
				User u = new User();
				u.setOlmId(body.getSubject());
				u.setRole((String) body.get("role"));
				u.setName((String) body.get("name"));
				u.setValidUpto(Long.parseLong((String) body.get("validUpto")));
				return u;
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException(new Status(401,"Unauthorized"), e);
			} 

    }
    
    public String generateToken(User u) {
    	Long validTill = new Date().getTime() + validDuration;
        Claims claims = Jwts.claims().setSubject(u.getOlmId());
        claims.put("validUpto", validTill + "");
        claims.put("role", u.getRole());
        claims.put("name", u.getName());
        claims.setNotBefore(new Date());
        claims.setExpiration(new Date(new Date().getTime()+validDuration));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
