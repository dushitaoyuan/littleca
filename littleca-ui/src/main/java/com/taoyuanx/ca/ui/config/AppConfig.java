package com.taoyuanx.ca.ui.config;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.taoyuanx.ca.ui.common.CAConstant;
import com.taoyuanx.ca.core.exception.CertException;
import com.taoyuanx.ca.core.util.CertUtil;
import com.taoyuanx.ca.ui.util.SimpleTokenManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 都市桃源
 * 项目配置
 */
@ConfigurationProperties(prefix = "app.config")
@Configuration
@Data
@Slf4j
public class AppConfig implements ApplicationRunner {
	/**
	 * caCert ca证书地址
	 * caPrivateKey ca私钥地址 
	 * clientCertBasePath 生成客户端证书存放地址
	 * signAlg  签名算法
	 *
	 * 系统账户密码
	 * username
	 * password
	 */
	private String certBaseDir;
	private  CaConfig rsa;
	private  CaConfig sm2;
	private  CaConfig dsa;
	private  CaConfig ecdsa;
	private String username;
	private String password;





	@Bean
	public SimpleTokenManager tokenManager(){
		return new SimpleTokenManager(password);
	}


	private Map<CAConstant.KeyType,CaConfig> allCaConfig=new HashMap<>();
	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
			try{
				//ca系统初始化
				allCaConfig.put(CAConstant.KeyType.RSA,readToConfig(rsa, CAConstant.KeyType.RSA));
				allCaConfig.put(CAConstant.KeyType.SM2,readToConfig(sm2, CAConstant.KeyType.SM2));
				allCaConfig.put(CAConstant.KeyType.DSA,readToConfig(dsa, CAConstant.KeyType.DSA));
				allCaConfig.put(CAConstant.KeyType.ECDSA,readToConfig(ecdsa, CAConstant.KeyType.ECDSA));
			}catch (Exception e){
				throw new RuntimeException("初始化系统失败",e);
			}
	}
	public CaConfig getByKeyType(Integer keyType){
		return  allCaConfig.get(CAConstant.KeyType.forValue(keyType));
	}




	public CaConfig readToConfig(CaConfig caConfig, CAConstant.KeyType keyType){
		try {
			//初始化证书配置
			if(Objects.nonNull(caConfig)){
				caConfig.setCertPath(forceBuildPath(caConfig.getCertPath()));
				caConfig.setPriPath(forceBuildPath(caConfig.getPriPath()));
				caConfig.setClientCertBasePath(forceBuildPath(caConfig.getClientCertBasePath()));
				X509Certificate caCertX509 = CertUtil.readX509Cert(caConfig.getCertPath());
				caConfig.setCert(caCertX509);
				caConfig.setIssuerDN(caCertX509.getIssuerDN().getName());
				caConfig.setPub(caCertX509.getPublicKey());
				caConfig.setPri(CertUtil.readPrivateKeyPem(caConfig.getPriPath()));
				log.info("{}初始化完毕,配置信息:{}",keyType.name,caConfig);
				return caConfig;
			}
		} catch (CertException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private String forceBuildPath(String oppositePath){
		File baseFileDir=new File(certBaseDir);
		File absFile=new File(baseFileDir,oppositePath);
		if(!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}
		if(absFile.isDirectory()){
			if(!absFile.exists()){
				absFile.mkdirs();
			}
		}else{
			if(!absFile.getParentFile().exists()){
				absFile.getParentFile().mkdirs();
			}
		}
		return  absFile.getAbsolutePath();
	}


}
