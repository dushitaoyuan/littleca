package com.taoyuanx.ca.service;

import com.taoyuanx.ca.common.CAConstant.KeyType;
import com.taoyuanx.ca.dto.CertReq;
import com.taoyuanx.ca.dto.CertResult;
import com.taoyuanx.ca.dto.KeyPairResult;

/**
 * @author 都市桃源
 * 2018年9月14日 下午1:37:18
 *证书服务
*/
public interface CertService {
	CertResult certToUser(CertReq req,Integer keySize) throws Exception;
	KeyPairResult createKeyPair(KeyType type,Integer keySize) throws Exception;
}
