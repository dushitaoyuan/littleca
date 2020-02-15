package com.taoyuanx.ca.bc;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 都市桃源 2018年7月1日下午4:41:23 x509 证书扩展信息生成工具
 */
public class X509CertExtensions {
	/**
	 * 证书CRL分布点
	 * 
	 * @return
	 */
	public static CRLDistPoint buildCRLDIstPoint() {
		return null;
	}

	/**
	 * 证书策略
	 * 
	 * @return
	 */
	public static ASN1EncodableVector buildPolicyInfo() {
		return null;
	}

	/**
	 * 增强密钥用法 见KeyPurposeId
	 */
	public static ExtendedKeyUsage builldExtendKeyUsage() {
		List<KeyPurposeId> list = new ArrayList<>();
		list.add(KeyPurposeId.id_kp_OCSPSigning);
		list.add(KeyPurposeId.id_kp_capwapAC);
		list.add(KeyPurposeId.id_kp_timeStamping);
		KeyPurposeId[] usages = new KeyPurposeId[list.size()];
		ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(list.toArray(usages));
		return extendedKeyUsage;
	}

	/**
	 * 密钥用法 见 KeyUsage
	 * 
	 * @return
	 */
	public static KeyUsage builldKeyUsage() {
		int keyUsage = KeyUsage.digitalSignature | KeyUsage.nonRepudiation | KeyUsage.keyEncipherment
				| KeyUsage.dataEncipherment | KeyUsage.dataEncipherment | KeyUsage.keyAgreement | KeyUsage.keyCertSign
				| KeyUsage.cRLSign | KeyUsage.encipherOnly | KeyUsage.decipherOnly;
		return new KeyUsage(keyUsage);
	}

	/**
	 * 主题备用名称扩展 参见:GeneralName
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ASN1Encodable buildSubjectAlternativeName(GeneralName... generalNames) throws Exception {
		if (null != generalNames) {
			return new GeneralNames(generalNames);
		}
		return null;

	}

	/**
	 * 颁发者备用别名扩展
	 * 
	 * @return
	 */
	public static ASN1EncodableVector buildIssuerAlternativeName(X509v3CertificateBuilder certBuilder)
			throws Exception {
		// Extension.issuerAlternativeName
		return null;
	}

	public static void buildAllExtensions(X509v3CertificateBuilder certBuilder, PublicKey userPublicKey,
			PublicKey caPublicKey) throws Exception {
		JcaX509ExtensionUtils utils = new JcaX509ExtensionUtils();
		// 添加CRL分布点
		/// certBuilder.addExtension(Extension.cRLDistributionPoints, true,
		// X509CertExtensions.buildCRLDIstPoint());
		// 添加证书策略
		// certBuilder.addExtension(Extension.certificatePolicies, true,
		// X509CertExtensions.buildPolicyInfo());
		// 颁发者密钥标识
		certBuilder.addExtension(Extension.authorityKeyIdentifier, false,
				utils.createAuthorityKeyIdentifier(caPublicKey));
		// 使用者密钥标识
		certBuilder.addExtension(Extension.subjectKeyIdentifier, false,
				utils.createSubjectKeyIdentifier(userPublicKey));
		// 密钥用法
		certBuilder.addExtension(Extension.keyUsage, true, X509CertExtensions.builldKeyUsage());
		// 增强密钥用法
		certBuilder.addExtension(Extension.extendedKeyUsage, true, X509CertExtensions.builldExtendKeyUsage());
		// 主题备用名称扩展
		/*certBuilder.addExtension(Extension.issuerAlternativeName, true, X509CertExtensions
				.buildSubjectAlternativeName(new GeneralName(GeneralName.rfc822Name, "email@163.com")));*/
		// 基本约束
		if (userPublicKey == caPublicKey) {
			certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(3));
		} else {
			certBuilder.addExtension(Extension.basicConstraints, false, new BasicConstraints(0));
		}

	}
}
