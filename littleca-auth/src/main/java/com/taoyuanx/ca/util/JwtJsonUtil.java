package com.taoyuanx.ca.util;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtJsonUtil {
	private static ObjectMapper mapper=null;
	static {
		mapper= new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
	}
	public static  String  serialize(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			  throw new JWTCreationException("Some of the Claims couldn't be converted to a valid JSON format.", e);
		}
	}
	public static  <T> T deserialize(String json,Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			  throw new JWTCreationException("Some of the Claims couldn't be converted to a valid JSON format.", e);
		}
	}
	
	
	
	
}
