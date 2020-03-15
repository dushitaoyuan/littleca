package example.cert;

import com.taoyuanx.ca.core.util.CertUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.security.PrivateKey;

/**
 * @author dushitaoyuan
 * @date 2020/3/522:46
 */
public class CertTest {
    @Test
    public void pkcs1Test() throws Exception {
        PrivateKey privateKey = CertUtil.readPrivateKeyPem("d://cert/client_pri.pem");
        PrivateKey privateKey2 = CertUtil.readPrivateKeyPkcs8Pem(FileUtils.readFileToString(new File("d://cert/client_pri_pkcs8.pem"), "UTF-8"));




    }




}
