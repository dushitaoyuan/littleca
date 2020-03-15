package com.taoyuanx.ca.core.sm.interfaces.impl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;

import com.taoyuanx.ca.core.sm.interfaces.Sm2KeyPair;
import com.taoyuanx.ca.core.sm.interfaces.Sm2PrivateKey;
import com.taoyuanx.ca.core.sm.interfaces.Sm2PublicKey;

public class Sm2KeyPairImpl implements Sm2KeyPair{
	private Sm2PublicKey publicKey;
	private Sm2PrivateKey privateKey;

	public Sm2KeyPairImpl(boolean selfgen) {
		SecureRandom random = new SecureRandom();
		ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(DOMAIN_PARAMS, random);
		ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
		keyGen.init(keyGenerationParams);
		AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();
		ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
		ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();
		ECDomainParameters domainParams = priKey.getParameters();
		ECParameterSpec spec = new ECParameterSpec(domainParams.getCurve(), domainParams.getG(), domainParams.getN(),
				domainParams.getH());
		BCECPublicKey bcecPublicKey = new BCECPublicKey(ALGO_NAME_EC, pubKey, spec, BouncyCastleProvider.CONFIGURATION);
		publicKey = new Sm2PublicKeyImpl(bcecPublicKey);
		privateKey = new Sm2PrivateKeyImpl(new BCECPrivateKey(ALGO_NAME_EC, priKey, bcecPublicKey, spec, BouncyCastleProvider.CONFIGURATION));
	}

	
	public Sm2KeyPairImpl() {
		  try {
			  KeyPairGenerator g = KeyPairGenerator.getInstance("EC", "BC");
			  //参见 SM2P256V1Curve GMObjectIdentifiers
			  g.initialize(new ECNamedCurveGenParameterSpec("sm2p256v1"));
			  KeyPair p = g.generateKeyPair();
		
			  PrivateKey privKey = p.getPrivate();
			  PublicKey pubKey = p.getPublic();
			  publicKey=new Sm2PublicKeyImpl( (BCECPublicKey) pubKey);
			  privateKey=new Sm2PrivateKeyImpl((BCECPrivateKey) privKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public Sm2PublicKey getPublic() {
		return publicKey;
	}

	public Sm2PrivateKey getPrivate() {
		return privateKey;
	}


}
