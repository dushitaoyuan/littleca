package com.taoyuanx.ca.shellui.config;

import com.taoyuanx.ca.shell.excutors.ShellExecutor;
import com.taoyuanx.ca.shell.excutors.impl.WindowsShellExecutor;
import com.taoyuanx.ca.shellui.util.SimpleTokenManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author 都市桃源
 * 项目配置
 */
@ConfigurationProperties(prefix = "app.config")
@Configuration
@Data
@Slf4j
public class AppConfig {
    /**
     * <p>
     * 系统账户密码
     * username
     * password
     */

    private String username;
    private String password;

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
     * 生成证书的基础目录
     */
    private String certBaseDir;

    /**
     * pkcs12 密码
     */
    private String certPassword;

    /**
     * rsa 位数
     */
    private String rsaBitNum = "1024";

    /**
     * cert 过期天数
     */
    private String certExpireDay = "3650";

    private String shellType;


    @Bean
    public SimpleTokenManager tokenManager() {
        return new SimpleTokenManager(password);
    }

    @Bean
    public ShellExecutor shellExecutor() {
        return new WindowsShellExecutor();
    }






    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCertBaseDir() {
        return certBaseDir;
    }

    public void setCertBaseDir(String certBaseDir) {
        this.certBaseDir = certBaseDir;
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

    public String getShellType() {
        return shellType;
    }

    public void setShellType(String shellType) {
        this.shellType = shellType;
    }
}
