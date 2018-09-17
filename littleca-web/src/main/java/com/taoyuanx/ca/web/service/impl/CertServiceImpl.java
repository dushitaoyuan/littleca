package com.taoyuanx.ca.web.service.impl;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoyuanx.ca.openssl.cert.CertUtil;
import com.taoyuanx.ca.web.common.CAConstant.KeyType;
import com.taoyuanx.ca.web.config.CaConfig;
import com.taoyuanx.ca.web.dto.CertReq;
import com.taoyuanx.ca.web.dto.CertResult;
import com.taoyuanx.ca.web.dto.KeyPairResult;
import com.taoyuanx.ca.web.service.CertService;
import com.taoyuanx.ca.web.util.KeyPairUtil;

@Service
public class CertServiceImpl implements CertService {
	@Autowired
	CaConfig caConfig;

	@Override
	public CertResult certToUser(CertReq req,Integer keySize) throws Exception {
		CertResult certResult = new CertResult();
		String signAlg = req.getSignAlg() == null ? caConfig.getDefaultSignAlg() : req.getSignAlg();
		KeyPairResult keyPairResult = KeyPairUtil.gen(caConfig.getCaType(), keySize);
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
