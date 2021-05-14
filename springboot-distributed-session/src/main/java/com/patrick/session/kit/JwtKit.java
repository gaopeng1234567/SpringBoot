package com.patrick.session.kit;

import com.patrick.session.config.properties.JwtProperties;
import com.patrick.session.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtKit {


	@Autowired
	private JwtProperties jwtProperties;

	/**
	 * 创建jwtToken
	 * @param member
	 * @return
	 */
	public String generateJwtToken(User member) {
		Map<String, Object> claims = new HashMap<>();

		claims.put("sub", member.getUsername());
		claims.put("createdate", new Date());

		return Jwts.builder()
				//.addClaims(claims) 上课异常原因,改成下面的方式
				.setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000))
				.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
				.compact();
	}


	/**
	 * 解析jwt
	 * @param jwtToken
	 * @return
	 */
	public Claims parseJwtToken(String jwtToken) {
		Claims claims = null;
		try {
			claims = Jwts.parser()
					.setSigningKey(jwtProperties.getSecret())
					//.parseClaimsJwt(jwtToken) 上课时的异常原因：不用用jwt解析，用下面的jws解析
					.parseClaimsJws(jwtToken)
					.getBody();
		} catch (ExpiredJwtException e) {
			System.out.println("JWT验证失败:token已经过期");
		} catch (UnsupportedJwtException e) {
			System.out.println("JWT验证失败:token格式不支持");
		} catch (MalformedJwtException e) {
			System.out.println("JWT验证失败:无效的token");
		} catch (SignatureException e) {
			System.out.println("JWT验证失败:无效的token");
		} catch (IllegalArgumentException e) {
			System.out.println("JWT验证失败:无效的token");
		}
		return claims;
	}

}
