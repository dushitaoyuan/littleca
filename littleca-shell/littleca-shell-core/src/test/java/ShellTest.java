import com.taoyuanx.ca.shell.excutors.ShellExecutor;
import com.taoyuanx.ca.shell.excutors.impl.LinuxShellExecutor;
import com.taoyuanx.ca.shell.excutors.impl.WindowsShellExecutor;
import com.taoyuanx.ca.shell.params.CertSubject;
import com.taoyuanx.ca.shell.params.ShellParam;
import com.taoyuanx.ca.shell.params.ShellType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author dushitaoyuan
 * @date 2020/3/25
 */
public class ShellTest {

    private ShellParam shellParam;

    @Before
    public void shellParam() {
        this.shellParam = new ShellParam();
        shellParam.setShellPath("D:\\soft\\OpenSSL-Win64\\myca\\cert_shell_auto.bat");
        shellParam.setCertPassword("123456");
        CertSubject certSubject = new CertSubject("CN", "BJ", "BJ", "taoyuanx.com",
                "taoyuanx.com", "*.taoyuanx.com");
        shellParam.setCertSubect(certSubject);
        shellParam.setOpensslConfPath("D:\\soft\\OpenSSL-Win64\\myca\\openssl.cfg");
        shellParam.setOpensslCaPrivateKeyPath("D:\\soft\\OpenSSL-Win64\\myca\\cacert.pem");
        shellParam.setCreateCertDir("D:\\soft\\OpenSSL-Win64\\myca\\myclient");
        shellParam.setShellType(ShellType.JAVA);
    }

    @Test
    public void execShell() throws IOException {

        ShellExecutor shellExecutor = new LinuxShellExecutor();
        shellExecutor.execute(shellParam);
    }
}
