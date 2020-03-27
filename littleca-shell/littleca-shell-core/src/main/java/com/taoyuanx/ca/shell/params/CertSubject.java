package com.taoyuanx.ca.shell.params;


/**
 * @author dushitaoyuan
 * @desc 证书subject 对象
 * @date 2020/3/25
 */
public class CertSubject {
    /**
     * C=国家代码(例如:中国CN)
     */
    private String C;
    /**
     * ST=省份(例如:北京BJ)
     */
    private String ST;
    /**
     * L=城市(例如:北京BJ)
     */
    private String L;
    /**
     * O=组织(例如:baidu)
     */
    private String O;
    /**
     * OU=单位(例如:baidu)
     */
    private String OU;
    /**
     * CN=域名(例如:*.baidu.com)
     */
    private String CN;

    public CertSubject() {
    }

    public CertSubject(String c, String ST, String l, String o, String OU, String CN) {
        this.C = c;
        this.ST = ST;
        this.L = l;
        this.O = o;
        this.OU = OU;
        this.CN = CN;
    }

    /**
     * openssl subject="/C=%C%/ST=%ST%/L=%L%/O=%O%/OU=%OU%/CN=%CN%"
     */
     private static String OPENSLL_SUBJECT_FORMAT = "\"/C=%s/ST=%s/L=%s/O=%s/OU=%s/CN=%s\"";


    /**
     * java C=CN,ST=BJ,L=BJ,O=testclient,OU=testclient,CN=client
     */
     private static String JAVA_SUBJECT_FORMAT = "\"C=%s,ST=%s,L=%s,O=%s,OU=%s,CN=%s\"";


    public String buildOpensslSubject() {
        return String.format(OPENSLL_SUBJECT_FORMAT, C, ST, L, O, OU, CN);
    }


    public String buildJavaSubject() {
        return String.format(JAVA_SUBJECT_FORMAT, C, ST, L, O, OU, CN);
    }


    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getO() {
        return O;
    }

    public void setO(String o) {
        O = o;
    }

    public String getOU() {
        return OU;
    }

    public void setOU(String OU) {
        this.OU = OU;
    }

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
