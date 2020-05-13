package com.taoyuanx.ca;

import com.auth0.jwt.algorithms.Algorithm;

import com.taoyuanx.ca.encode.api.Encode;
import com.taoyuanx.ca.exception.TokenException;
import com.taoyuanx.ca.token.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 都市桃源 2018年5月23日下午10:09:16
 * token操作类
 */
public interface TokenManager {

	void setTokenConfig(TokenConfig config);

	/**
	 * 生成token
	 * 
	 * @param token
	 * @return
	 * @throws TokenException
	 */
	String create(Token token) throws TokenException;

	/**
	 * 验证token
	 * 
	 * @param token
	 * @return
	 * @throws TokenException
	 */
	Token verify(String token) throws TokenException;

	/**
	 * 敏感数据加密
	 * 
	 * @param content
	 * @return
	 * @throws TokenException
	 */
	String encode(String content) throws TokenException;

	/**
	 * 敏感数据解密
	 * 
	 * @return
	 * @throws TokenException
	 */
	String decode(String encode) throws TokenException;

	public static class Builder {
		private Map<String, Object> config = new HashMap<>(5);
		private static final String alg_ = "alg", vld_ = "vld", iss_ = "iss", sub_ = "sub", cfg_ = "cfg",encode_="encode";

		public Builder() {
		}

		public Builder buildAlg(Algorithm algorithm) {
			config.put(alg_, algorithm);
			return this;
		}

		public Builder buildValid(Integer time, TimeUnit unit) {
			config.put(vld_, unit.toMillis(time));
			return this;
		}
		public Builder buildValid(Long time) {
			config.put(vld_, time);
			return this;
		}
		public Builder buildIss(String iss) {
			config.put(iss_, iss);
			return this;
		}

		public Builder buildSub(String sub) {
			config.put(sub_, sub);
			return this;
		}
		public Builder buildEncode(Encode encode) {
			config.put(encode_, encode);
			return this;
		}
		public Builder buildConfig(TokenConfig tokenConfig) {
			config.put(cfg_, tokenConfig);
			return this;
		}

		public TokenManager build() {
			DefaultTokenManager manager = new DefaultTokenManager();
			if (config.containsKey(cfg_)) {
				manager.setTokenConfig((TokenConfig) config.get(cfg_));
			} else {
				TokenConfig cfg = new TokenConfig();
				cfg.setAlgorithm((Algorithm) config.get(alg_));
				Long vld = (Long) config.get(vld_);
				if (null == vld) {// 30分钟
					vld = TimeUnit.MINUTES.toMillis(30);
				}
				cfg.setValidTime(vld);
				cfg.setDefaultIss((String) config.get(iss_));
				cfg.setDefaultSub((String) config.get(sub_));
				cfg.setEncode((Encode) config.get(encode_));
				manager.setTokenConfig(cfg);
			}
			return manager;
		}

	}
}
