package com.taoyuanx.ca.tsa.client.impl;

import com.taoyuanx.ca.tsa.ex.LittleTsaException;
import com.taoyuanx.ca.tsa.TsaTranserConstant;
import com.taoyuanx.ca.tsa.client.TsaClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * @author dushitaoyuan
 * @desc 时间戳客户端
 * @date 2019/7/10
 */
public class LittleTsaClientImpl implements TsaClient {
    static Logger LOG = LoggerFactory.getLogger(LittleTsaClientImpl.class);
    private String dataType;
    private DigestAlgorithmIdentifierFinder digestAlgorithmIdentifierFinder = new DefaultDigestAlgorithmIdentifierFinder();
    private String tsaUrl;
    private OkHttpClient client;
    private boolean isBase64;

    private String username;
    private String password;
    private boolean usernameEnabled;

    public LittleTsaClientImpl(String encoding, String tsaUrl, OkHttpClient client) {
        this.isBase64 = TsaTranserConstant.isBase64(encoding);
        this.dataType = isBase64 ? TsaTranserConstant.TRANSFER_ENCODING_BASE64 : TsaTranserConstant.TRANSFER_ENCODING_BINARY;
        this.tsaUrl = tsaUrl;
        this.client = client;

    }

    public LittleTsaClientImpl(String encoding, String tsaUrl, OkHttpClient client, String username, String password) {
        this(encoding, tsaUrl, client);
        this.usernameEnabled = true;
        this.username = username;
        this.password = password;

    }

    @Override
    public TimeStampResponse timestamp(String signAlg, byte[] messageDigest) {
        AlgorithmIdentifier algorithmIdentifier = digestAlgorithmIdentifierFinder.find(signAlg.toUpperCase());
        if (algorithmIdentifier == null) {
            throw new LittleTsaException(signAlg + " not support");
        }
        Response response = null;
        try {
            TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
            tsqGenerator.setCertReq(true);
            BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
            TimeStampRequest timeStampRequest = tsqGenerator.generate(TSPAlgorithms.SHA256, messageDigest);
            byte[] requestBytes = timeStampRequest.getEncoded();
            if (isBase64) {
                requestBytes = Base64.encodeBase64(requestBytes);
            }
            Request.Builder builder = new Request.Builder().addHeader("Content-Type", TsaTranserConstant.TIMESTAMP_QUERY_CONTENT_TYPE)
                    .addHeader(TsaTranserConstant.TRANSFER_ENCODING_HEADER, dataType).url(tsaUrl)
                    .post(RequestBody.create(requestBytes));
            if (usernameEnabled) {
                builder.addHeader("username", username).addHeader("password", password);
            }
            Request timeStampOkHttpRequest = builder.build();
            response = client.newCall(timeStampOkHttpRequest).execute();
            if (response.isSuccessful()) {
                byte[] timeStampResponse = response.body().bytes();
                if (isBase64) {
                    timeStampResponse = Base64.decodeBase64(timeStampResponse);
                }
                return new TimeStampResponse(timeStampResponse);
            }
            throw new LittleTsaException("little tsa call failed, result" + response.body().string());
        } catch (Exception e) {
            LOG.error("little tsa exception {}", e);
            if (e instanceof LittleTsaException) {
                throw (LittleTsaException) e;
            }
            throw new LittleTsaException("little tsa exception", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
