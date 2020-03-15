package com.taoyuanx.ca.ui.service.impl;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import com.taoyuanx.ca.ui.config.AppConfig;
import com.taoyuanx.ca.ui.config.CaConfig;
import com.taoyuanx.ca.ui.common.CAConstant;
import com.taoyuanx.ca.core.util.CertUtil;
import com.taoyuanx.ca.ui.util.KeyPairUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoyuanx.ca.ui.dto.CertReq;
import com.taoyuanx.ca.ui.dto.CertResult;
import com.taoyuanx.ca.ui.dto.KeyPairResult;
import com.taoyuanx.ca.ui.service.CertService;

@Service
public class CertServiceImpl implements CertService {
	@Autowired
    AppConfig appConfig;
	@Override
	public CertResult certToUser(CertReq req,Integer keySize) throws Exception {
		CaConfig caConfig = appConfig.getByKeyType(req.getCatype());
		CertResult certResult = new CertResult();
		String signAlg = req.getSignAlg() == null ? caConfig.getSignAlg() : req.getSignAlg();
		KeyPairResult keyPairResult = KeyPairUtil.gen(CAConstant.KeyType.forValue(req.getCatype()), keySize);
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
	public KeyPairResult createKeyPair(CAConstant.KeyType type, Integer keySize) throws Exception {
		KeyPairResult keyPairResult = KeyPairUtil.gen(type, keySize);
		return keyPairResult;
	}


}
