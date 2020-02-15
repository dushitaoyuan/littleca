package com.taoyuanx.test;

import com.taoyuanx.ca.AppServerApplication;
import com.taoyuanx.ca.config.LittleTsaConfig;
import com.taoyuanx.ca.ex.LittleTsaException;
import com.taoyuanx.ca.tsa.TimeStampService;
import com.taoyuanx.ca.tsa.TsaTranserConstant;
import com.taoyuanx.ca.tsa.client.TsaClient;
import com.taoyuanx.ca.tsa.client.impl.LittleTsaClientImpl;
import okhttp3.OkHttpClient;
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
import org.bouncycastle.operator.*;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.tsp.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc 客户端测试
 * @date 2019/7/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class LittleTsaClientTest {
    private TsaClient tsaClient;
    @Autowired
    TimeStampService timeStampService;

    @Autowired
    LittleTsaConfig littleTsaConfig;

    @Before
    public void before() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(50, TimeUnit.SECONDS).readTimeout(200, TimeUnit.SECONDS).build();
        String tsaUrl = "http://localhost:8081/tsa";
        tsaClient = new LittleTsaClientImpl(TsaTranserConstant.TRANSFER_ENCODING_BASE64, tsaUrl, okHttpClient, "little-tsa", "little-tsa");
        // tsaClient = new LittleTsaClientImpl(TsaTranserConstant.TRANSFER_ENCODING_BASE64, tsaUrl, okHttpClient);

    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void clientTest() throws Exception {
        String signAlg = "SHA-256";
        TimeStampResponse sha256 = tsaClient.timestamp(signAlg, getDigest("123".getBytes(), signAlg));
        System.out.println(sha256);
    }

    public byte[] getDigest(byte[] data, String signAlg) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(signAlg);
        return digest.digest(data);
    }

    @Test
    public void timestampTest() throws Exception {
        TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
        tsqGenerator.setCertReq(true);
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        String signAlg = "SHA-256";
        byte[] digest = getDigest("123".getBytes(), signAlg);
        TimeStampRequest timeStampRequest = tsqGenerator.generate(TSPAlgorithms.SHA256, digest, nonce);

        TimeStampResponse timestamp = timeStampService.timestamp(timeStampRequest);
        System.out.println(timestamp);
    }

}
