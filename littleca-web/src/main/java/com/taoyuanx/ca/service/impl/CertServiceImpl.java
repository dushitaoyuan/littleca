package com.taoyuanx.ca.service.impl;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import com.taoyuanx.ca.config.AppConfig;
import com.taoyuanx.ca.config.CaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoyuanx.ca.openssl.cert.CertUtil;
import com.taoyuanx.ca.common.CAConstant.KeyType;
import com.taoyuanx.ca.dto.CertReq;
import com.taoyuanx.ca.dto.CertResult;
import com.taoyuanx.ca.dto.KeyPairResult;
import com.taoyuanx.ca.service.CertService;
import com.taoyuanx.ca.util.KeyPairUtil;

@Service
public class CertServiceImpl implements CertService {
	@Autowired
    AppConfig appConfig;
	@Override
	public CertResult certToUser(CertReq req,Integer keySize) throws Exception {
		CaConfig caConfig = appConfig.getByKeyType(req.getCatype());
		CertResult certResult = new CertResult();
		String signAlg = req.getSignAlg() == null ? caConfig.getSignAlg() : req.getSignAlg();
		KeyPairResult keyPairResult = KeyPairUtil.gen(KeyType.forValue(req.getCatype()), keySize);
		PublicKey pub = keyPairResult.getPub();
		PrivateKey pri = keyPairResult.getPri();
		X509Certificate userCert = CertUtil.makeUserCert(pub, caConfig.getPub(), caConfig.getPri(),
				caConfig.getIssuerDN(), req.getUserDN(), req.getNotBefore(), req.getNotAfter(),
				new BigInteger(req.getSerialNumber()), signAlg);
		certResult.setPri(pri);
		certResult.setPub(pub);
		certResult.setCert(userCert);
		return certResult;

	}

	@Override
	public KeyPairResult createKeyPair(KeyType type,Integer keySize) throws Exception {
		KeyPairResult keyPairResult = KeyPairUtil.gen(type, keySize);
		return keyPairResult;
	}


}
