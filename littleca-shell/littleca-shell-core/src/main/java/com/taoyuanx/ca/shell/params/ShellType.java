package com.taoyuanx.ca.shell.params;

/**
 * @author dushitaoyuan
 * @desc shell类型
 * @date 2020/3/25
 */
public enum ShellType {
    JAVA("java_create"), OPENSSL_WINDOWS("openssl_create"), OPENSSL_LINUX("openssl_create");
    public String shellFunctionName;

    ShellType(String functionName) {
        this.shellFunctionName = functionName;
    }


}
