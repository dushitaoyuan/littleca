package com.taoyuanx.ca;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.taoyuanx.ca.encode.eum.StringByteType;
import com.taoyuanx.ca.exception.ExceptionType;
import com.taoyuanx.ca.exception.TokenException;
import com.taoyuanx.ca.token.PrivateClaims;
import com.taoyuanx.ca.token.PublicClaims;
import com.taoyuanx.ca.token.Token;
import com.taoyuanx.ca.util.BytesStringUtil;
import com.taoyuanx.ca.util.Utils;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings(value = "all")
public class DefaultTokenManager implements TokenManager {
	private TokenConfig tokenConfig;

	private static final String PRI_SPILIT = ";", VALUE_SPILIT = "=";

	@Override
	public String create(Token token) throws TokenException {
		if (null == token) {
			throw new TokenException(ExceptionType.FORMAT, null);
		}
		Long now = System.currentTimeMillis();
		com.auth0.jwt.JWTCreator.Builder jwtBuilder = JWT.create();
		if (Utils.isNotEmpty(token.getAud())) {
			jwtBuilder.withAudience(token.getAud());
		}
		if (Utils.isNotEmpty(token.getExp())) {
			jwtBuilder.withExpiresAt(new Date(token.getExp()));
		} else {
			Long exp = now + this.tokenConfig.getValidTime();
			token.setExp(exp);
			jwtBuilder.withExpiresAt(new Date(exp));
		}
		if(Utils.isNotEmpty(token.getIat())) {
			jwtBuilder.withIssuedAt(new Date(now));
		}
		if (Utils.isNotEmpty(token.getIss())) {
			jwtBuilder.withIssuer(token.getIss());
		} else {
			String default_iss = this.tokenConfig.getDefaultIss();
			token.setIss(default_iss);
			jwtBuilder.withIssuer(default_iss);
		}
		if (Utils.isNotEmpty(token.getJti())) {
			jwtBuilder.withJWTId(token.getJti());
		}
		if (Utils.isNotEmpty(token.getSub())) {
			jwtBuilder.withSubject(token.getSub());
		} else {
			String default_sub = this.tokenConfig.getDefaultSub();
			token.setSub(default_sub);
			jwtBuilder.withSubject(default_sub);
		}
		if (Utils.isNotEmpty(token.getNbf())) {
			jwtBuilder.withNotBefore(new Date(token.getNbf()));
		}
		Map<String, String> pri = token.getPri();
		if (Utils.isNotEmpty(pri)) {
			jwtBuilder.withClaim(PrivateClaims.PRI, encode(mapToString(pri)));
		}
		
		Map<String, String> pub = token.getPub();
		if (Utils.isNotEmpty(pub)) {
			jwtBuilder.withClaim(PublicClaims.PUB,mapToString(pub));
		}
		return jwtBuilder.sign(this.tokenConfig.getAlgorithm());
	}

	@Override
	public Token verify(String token) throws TokenException {
		try {
			DecodedJWT verify = JWT.require(this.tokenConfig.getAlgorithm()).acceptLeeway(0).build()
					.verify(token);
			Token tokenObj = new Token();
			tokenObj.setAlg(verify.getAlgorithm());
			tokenObj.setTyp(verify.getType());
			List<String> aud = verify.getAudience();
			if (Utils.isNotEmpty(aud)) {
				tokenObj.setAud(verify.getAudience().toArray(new String[aud.size()]));
			}
			tokenObj.setExp(verify.getExpiresAt().getTime());
			if (Utils.isNotEmpty(verify.getIssuedAt())) {
				tokenObj.setIat(verify.getIssuedAt().getTime());
			}
			tokenObj.setJti(verify.getId());
			if (Utils.isNotEmpty(verify.getNotBefore())) {
				tokenObj.setNbf(verify.getNotBefore().getTime());
			}
			tokenObj.setSub(verify.getSubject());
			tokenObj.setIss(verify.getIssuer());
			String pub = verify.getClaim(PublicClaims.PUB).asString();
			if(Utils.isNotEmpty(pub)) {
				Map<String,String> pubMap = stringToMap(pub);
				tokenObj.setPub(pubMap);
			}
			String pri = verify.getClaim(PrivateClaims.PRI).asString();
			if(Utils.isEmpty(pri)) {
				return tokenObj;
			}
			String decode = decode(pri);
			tokenObj.setPri(stringToMap(decode));
			return tokenObj;
		} catch (TokenExpiredException e) {
			throw new TokenException(ExceptionType.EXPIRE, e.getMessage());
		} catch (InvalidClaimException e) {
			String msg = e.getMessage();
			if (msg.indexOf("can't be used before") > 0) {
				throw new TokenException(ExceptionType.NOTBEFORE, e.getMessage());
			}
		} catch (JWTDecodeException e) {
			throw new TokenException(ExceptionType.FORMAT, e.getMessage());
		} catch (SignatureVerificationException e) {
			throw new TokenException(ExceptionType.SIGN, e.getMessage());
		}
		return null;
	}

	@Override
	public String encode(String content) throws TokenException {
		try {
			return BytesStringUtil.toString(tokenConfig.getEncode().encode(content.getBytes()), StringByteType.BA64_URL_SAFE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public String decode(String encode) throws TokenException {
		try {
			return new String(tokenConfig.getEncode().decode(BytesStringUtil.toBytes(encode,  StringByteType.BA64_URL_SAFE)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setTokenConfig(TokenConfig config) {
		this.tokenConfig = config;
	}

	public TokenConfig getTokenConfig() {
		return tokenConfig;
	}
	
	public String mapToString(Map<String,String> map) {
		StringBuilder buf = new StringBuilder();
		int count = 0, len = map.size() - 1;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			buf.append(entry.getKey()).append(VALUE_SPILIT).append(entry.getValue());
			if (count < len) {
				buf.append(PRI_SPILIT);
			}
			count++;
		}
		return buf.toString();
	}
	
	public Map<String,String> stringToMap(String str) {
		String[] split = str.split(PRI_SPILIT);
		Map<String,String> map = new HashMap<>(split.length);
		for (String part : split) {
			String[] keyValue = part.split(VALUE_SPILIT);
			map.put(keyValue[0], keyValue[1]);
		}
		return map;
	}

	
}
