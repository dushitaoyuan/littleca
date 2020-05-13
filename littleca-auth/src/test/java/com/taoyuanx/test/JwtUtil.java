package com.taoyuanx.test;

import com.auth0.jwt.algorithms.Algorithm;

import com.taoyuanx.ca.TokenManager;
import com.taoyuanx.ca.exception.TokenException;
import com.taoyuanx.ca.token.PublicClaims;
import com.taoyuanx.ca.token.Token;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtUtil {

	@Test
	public  void jwtTest() throws Exception {
		Algorithm hmac256 = Algorithm.HMAC256("taoyuanx");
		TokenManager tokenManager =new TokenManager.Builder().buildAlg(hmac256).buildValid(2, TimeUnit.HOURS).build();
		Token token=new Token();
		token.cliamsPub(PublicClaims.SESSIONID, UUID.randomUUID().toString());
		token.cliamsPub(PublicClaims.USERID, 123);
		String tokenStr = tokenManager.create(token);
		System.out.println(tokenStr);
		try {
			Token verify = tokenManager.verify(tokenStr);
			System.out.println(verify.getPubCliams(PublicClaims.SESSIONID, String.class));
			System.out.println(verify.getPubCliams(PublicClaims.USERID, Long.class));
			System.out.println(verify);
		}catch (TokenException e) {//异常
			System.out.println(e.type);
		}
	}

	
	
}
