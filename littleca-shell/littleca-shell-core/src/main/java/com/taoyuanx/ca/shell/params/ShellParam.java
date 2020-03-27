package com.taoyuanx.ca.shell.params;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @desc shell参数
 * @date 2020/3/24
 */
public class ShellParam {
    /**
     * 脚本地址
     */
    private String shellPath;
    /**
     * openssl 配置地址
     */
    private String opensslConfPath;
    /**
     * 证书签名地址
     */
    private String opensslCaPrivateKeyPath;
    /**
     * 生成证书的存储目录
     */
    private String createCertDir;
    /**
     * 证书 subject
     */
    private CertSubject certSubect;
    /**
     * pkcs12 密码
     */
    private String certPassword = "123456";

    /**
     * rsa 位数
     */
    private String rsaBitNum = "1024";

    /**
     * cert 过期天数
     */
    private String certExpireDay = "3650";

    private ShellType shellType;

    public List<String> buildShellParams() {
        Map<Integer, String> argsMap = new TreeMap<>(Integer::compareTo);
        argsMap.put(0, shellType.shellFunctionName);
        argsMap.put(1, createCertDir);
        argsMap.put(3, certPassword);
        argsMap.put(4, rsaBitNum);
        argsMap.put(5, certExpireDay);
        switch (shellType) {
            case JAVA:
                argsMap.put(2, certSubect.buildJavaSubject());
                break;
            case OPENSSL_WINDOWS:
            case OPENSSL_LINUX:
                argsMap.put(2, certSubect.buildOpensslSubject());
                /**
                 * openssl
                 */
                argsMap.put(6, opensslConfPath);
                argsMap.put(7, opensslCaPrivateKeyPath);
                break;
        }
        return argsMap.entrySet().stream().map(entry -> {
            return entry.getValue();
        }).collect(Collectors.toList());
    }


    public String getShellPath() {
        return shellPath;
    }

    public void setShellPath(String shellPath) {
        this.shellPath = shellPath;
    }

    public String getOpensslConfPath() {
        return opensslConfPath;
    }

    public void setOpensslConfPath(String opensslConfPath) {
        this.opensslConfPath = opensslConfPath;
    }

    public String getOpensslCaPrivateKeyPath() {
        return opensslCaPrivateKeyPath;
    }

    public void setOpensslCaPrivateKeyPath(String opensslCaPrivateKeyPath) {
        this.opensslCaPrivateKeyPath = opensslCaPrivateKeyPath;
    }

    public String getCreateCertDir() {
        return createCertDir;
    }

    public void setCreateCertDir(String createCertDir) {
        this.createCertDir = createCertDir;
    }

    public CertSubject getCertSubect() {
        return certSubect;
    }

    public void setCertSubect(CertSubject certSubect) {
        this.certSubect = certSubect;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public String getRsaBitNum() {
        return rsaBitNum;
    }

    public void setRsaBitNum(String rsaBitNum) {
        this.rsaBitNum = rsaBitNum;
    }

    public String getCertExpireDay() {
        return certExpireDay;
    }

    public void setCertExpireDay(String certExpireDay) {
        this.certExpireDay = certExpireDay;
    }

    public ShellType getShellType() {
        return shellType;
    }

    public void setShellType(ShellType shellType) {
        this.shellType = shellType;
    }
}
