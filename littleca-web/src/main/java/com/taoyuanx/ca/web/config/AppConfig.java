package com.taoyuanx.ca.web.config;

import java.security.cert.X509Certificate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.taoyuanx.ca.exception.CertException;
import com.taoyuanx.ca.openssl.cert.CertUtil;
import com.taoyuanx.ca.web.common.CAConstant.KeyType;
import com.taoyuanx.ca.web.util.SimpleTokenManager;

/**
 * @author 都市桃源
 * 项目配置
 */
@ConfigurationProperties(prefix = "app.config")
@Configuration
public class AppConfig {
	/**
	 * caCert ca证书地址
	 * caPrivateKey ca私钥地址 
	 * clientCertBasePath 生成客户端证书存放地址
	 * signAlg  签名算法
	 */
	private String caCert;
	private String caPrivateKey;
	private String clientCertBasePath;
	private String caType;
	private String signAlg;
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSignAlg() {
		return signAlg;
	}
	public void setSignAlg(String signAlg) {
		this.signAlg = signAlg;
	}
	public String getCaType() {
		return caType;
	}
	public void setCaType(String caType) {
		this.caType = caType;
	}
	
	public String getCaCert() {
		return caCert;
	}
	public void setCaCert(String caCert) {
		this.caCert = caCert;
	}
	public String getCaPrivateKey() {
		return caPrivateKey;
	}
	public void setCaPrivateKey(String caPrivateKey) {
		this.caPrivateKey = caPrivateKey;
	}
	public String getClientCertBasePath() {
		return clientCertBasePath;
	}
	public void setClientCertBasePath(String clientCertBasePath) {
		this.clientCertBasePath = clientCertBasePath;
	}
	@Bean
	@ConditionalOnBean(value=AppConfig.class)
	public CaConfig configCa(){
		try {
			CaConfig caConfig=new CaConfig();
			X509Certificate caCertX509 = CertUtil.readX509Cert(caCert);
			caConfig.setCert(caCertX509);
			if(StringUtils.isEmpty(signAlg)){
				caConfig.setDefaultSignAlg(caCertX509.getSigAlgName());
			}else{
				caConfig.setDefaultSignAlg(signAlg);
			}
			caConfig.setIssuerDN(caCertX509.getIssuerDN().getName());
			caConfig.setPub(caCertX509.getPublicKey());
			caConfig.setPri(CertUtil.readPrivateKeyPem(caPrivateKey));
			caConfig.setCaType(KeyType.valueOf(caType));
			return caConfig;
		} catch (CertException e) {
			throw new RuntimeException(e);
		}
		
	} 
	
	@Bean
	@ConditionalOnBean(value=AppConfig.class)
	public SimpleTokenManager tokenManager(){
		return new SimpleTokenManager(password);
		
	} 
	

}
