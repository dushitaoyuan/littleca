package com.taoyuanx.ca.web.service;

import com.taoyuanx.ca.web.common.CAConstant.KeyType;
import com.taoyuanx.ca.web.dto.CertReq;
import com.taoyuanx.ca.web.dto.CertResult;
import com.taoyuanx.ca.web.dto.KeyPairResult;

/**
 * @author 都市桃源
 * 2018年9月14日 下午1:37:18
 *证书服务
*/
public interface CertService {
	CertResult certToUser(CertReq req,Integer keySize) throws Exception;
	KeyPairResult createKeyPair(KeyType type,Integer keySize) throws Exception;
}
