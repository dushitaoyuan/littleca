package com.taoyuanx.ca.config;

import com.taoyuanx.ca.openssl.cert.CertUtil;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;

/**
 * @author dushitaoyuan
 * @desc tsa配置类
 * @date 2019/7/10
 */
@Configuration
@ConfigurationProperties("little.tsa")
public class LittleTsaConfig implements ApplicationRunner {
    /**
     * certKeystorePath tsaKeystore地址
     * keystorePassword Keystore密码
     * keystoreAlias Keystore存储别名
     * keystoreCertAlias  Keystore cert存储别名
     * 时间戳服务器用户密码
     * tsaUsername
     * tsaPassword
     */
    private String certKeystorePath;
    private String keystorePassword;
    private String keystoreAlias;
    private String keystoreCertAlias;
    private String tsaUsername;
    private String tsaPassword;

    public String getCertKeystorePath() {
        return certKeystorePath;
    }

    public void setCertKeystorePath(String certKeystorePath) {
        this.certKeystorePath = certKeystorePath;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getKeystoreAlias() {
        return keystoreAlias;
    }

    public void setKeystoreAlias(String keystoreAlias) {
        this.keystoreAlias = keystoreAlias;
    }

    public String getKeystoreCertAlias() {
        return keystoreCertAlias;
    }

    public void setKeystoreCertAlias(String keystoreCertAlias) {
        this.keystoreCertAlias = keystoreCertAlias;
    }

    public String getTsaUsername() {
        return tsaUsername;
    }

    public void setTsaUsername(String tsaUsername) {
        this.tsaUsername = tsaUsername;
    }

    public String getTsaPassword() {
        return tsaPassword;
    }

    public void setTsaPassword(String tsaPassword) {
        this.tsaPassword = tsaPassword;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream keyStoreStream = null;
        if (certKeystorePath.startsWith("classpath:")) {
            keyStoreStream = getClass().getClassLoader().getResourceAsStream(certKeystorePath.replaceFirst("classpath:", ""));
        } else {
            keyStoreStream = new FileInputStream(certKeystorePath);
        }
        KeyStore keyStore = CertUtil.readKeyStore(keyStoreStream, CertUtil.guessKeystoreType(certKeystorePath), keystorePassword);
        x509Certificate = (X509Certificate) keyStore.getCertificate(keystoreCertAlias);
        checkTsaCert(x509Certificate);
        privateKey = CertUtil.getPrivateKey(keyStore,keystorePassword, keystoreAlias);
    }

    private void checkTsaCert(X509Certificate x509Certificate) throws Exception {
        Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
        if (!criticalExtensionOIDs.contains(Extension.extendedKeyUsage.toString())) {
            throw new KeyStoreException("证书未包含  extended key usage 信息");
        }
        List<String> extendedKeyUsage = x509Certificate.getExtendedKeyUsage();
        if (!extendedKeyUsage.contains(KeyPurposeId.id_kp_timeStamping.getId())) {
            throw new KeyStoreException("证书 extended key usage 未包含 id_kp_timeStamping");
        }
        certSerialNumber=x509Certificate.getSerialNumber();
    }

    private X509Certificate x509Certificate;
    private PrivateKey privateKey;
    private BigInteger certSerialNumber;

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public BigInteger getCertSerialNumber() {
        return certSerialNumber;
    }
}
