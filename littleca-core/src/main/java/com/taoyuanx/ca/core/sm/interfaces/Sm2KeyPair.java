package com.taoyuanx.ca.core.sm.interfaces;

import java.math.BigInteger;

import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

/**
 * @author 都市桃源
 * 2018年9月13日 上午11:14:11
 * userfor:  sm2keypair
 */
public interface Sm2KeyPair {
	//////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 以下为SM2推荐曲线参数
	 */
	public final static BigInteger SM2_ECC_P = new BigInteger(
			"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16);
	public final static BigInteger SM2_ECC_A = new BigInteger(
			"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);
	public final static BigInteger SM2_ECC_B = new BigInteger(
			"28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);
	public final static BigInteger SM2_ECC_N = new BigInteger(
			"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);
	public final static BigInteger SM2_ECC_GX = new BigInteger(
			"32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
	public final static BigInteger SM2_ECC_GY = new BigInteger(
			"BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
	public static final ECCurve CURVE = new ECCurve.Fp(SM2_ECC_P, SM2_ECC_A, SM2_ECC_B, null, null);
	public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
	public static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, G_POINT, SM2_ECC_N,
			BigInteger.ONE);

	public static final byte[] WITHID = "1234567812345678".getBytes();
	//////////////////////////////////////////////////////////////////////////////////////

	public static final int SM3_DIGEST_LENGTH = 32;
	public static final String ALGO_NAME_EC = "EC";
	
	 Sm2PublicKey getPublic();

	 Sm2PrivateKey getPrivate() ;
}
