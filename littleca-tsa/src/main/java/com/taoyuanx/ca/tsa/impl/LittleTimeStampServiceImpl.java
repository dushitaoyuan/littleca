package com.taoyuanx.ca.tsa.impl;

import com.taoyuanx.ca.core.bc.ProviderInstance;
import com.taoyuanx.ca.tsa.config.LittleTsaConfig;
import com.taoyuanx.ca.tsa.ex.LittleTsaException;
import com.taoyuanx.ca.tsa.TimeStampService;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cms.DefaultSignedAttributeTableGenerator;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.SimpleAttributeTableGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.tsp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.Date;
import java.util.Hashtable;

/**
 * @author dushitaoyuan
 * @desc 时间戳服务实现
 * @date 2019/7/10
 */
@Service
public class LittleTimeStampServiceImpl implements TimeStampService {
    static {
        Security.addProvider(ProviderInstance.getBCProvider());
    }

    LittleTsaConfig littleTsaConfig;

    @Autowired
    public LittleTimeStampServiceImpl(LittleTsaConfig littleTsaConfig) {
        this.littleTsaConfig = littleTsaConfig;
    }


    @Override
    public TimeStampResponse timestamp(TimeStampRequest timestampRequest) {
        try {
            Date now = new Date();
            TimeStampTokenGenerator tokenGenerator = createTokenGenerator(timestampRequest, now);

            TimeStampResponseGenerator respGen = new TimeStampResponseGenerator(
                    tokenGenerator, TSPAlgorithms.ALLOWED);
            TimeStampResponse response = respGen.generate(timestampRequest, littleTsaConfig.getCertSerialNumber(), now);
            return response;
        } catch (Exception e) {
            throw new LittleTsaException("little tsa exception", e);
        }
    }

    private TimeStampTokenGenerator createTokenGenerator(TimeStampRequest request, Date tsDate) throws Exception {
        JcaSignerInfoGeneratorBuilder signBuilder;
        signBuilder = new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build());

        ASN1EncodableVector signedAttributes = new ASN1EncodableVector();
        signedAttributes.add(new Attribute(CMSAttributes.contentType, new DERSet(new ASN1ObjectIdentifier("1.2.840.113549.1.7.1"))));
        signedAttributes.add(new Attribute(CMSAttributes.messageDigest, new DERSet(new DEROctetString(request.getMessageImprintDigest()))));
        signedAttributes.add(new Attribute(CMSAttributes.signingTime, new DERSet(new DERUTCTime(tsDate))));
        AttributeTable signedAttributesTable = new AttributeTable(signedAttributes);
        signedAttributesTable.toASN1EncodableVector();
        DefaultSignedAttributeTableGenerator signedAttributeGenerator = new DefaultSignedAttributeTableGenerator(signedAttributesTable);
        signBuilder.setSignedAttributeGenerator(signedAttributeGenerator);
        signBuilder.setUnsignedAttributeGenerator(new SimpleAttributeTableGenerator(new AttributeTable(new Hashtable<String, String>())));

        SignerInfoGenerator signerInfoGen = signBuilder.build(new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider(BouncyCastleProvider.PROVIDER_NAME).build(littleTsaConfig.getPrivateKey()), littleTsaConfig.getX509Certificate());

        DigestCalculatorProvider build = new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build();
        DigestAlgorithmIdentifierFinder digestAlgorithmIdentifierFinder = new DefaultDigestAlgorithmIdentifierFinder();

        AlgorithmIdentifier sha256 = digestAlgorithmIdentifierFinder.find("SHA256");
        DigestCalculator digestCalculator = build.get(sha256);
        return new TimeStampTokenGenerator(signerInfoGen, digestCalculator, new ASN1ObjectIdentifier("1.2.3"));

    }

}
