import com.taoyuanx.ca.shell.excutors.ShellExecutor;
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
        shellParam.setShellPath("G:\\openssl\\cert_shell_auto.bat");
        shellParam.setCertPassword("123456");
        CertSubject certSubject = new CertSubject("CN", "BJ", "BJ", "taoyuanx.com",
                "taoyuanx.com", "*.taoyuanx.com");
        shellParam.setCertSubect(certSubject);
        shellParam.setOpensslConfPath("g:\\openssl\\ca\\openssl.cnf");
        shellParam.setOpensslCaPrivateKeyPath("g:\\openssl\\ca\\cacert.pem");
        shellParam.setCreateCertDir("g:\\openssl\\myclient");
        shellParam.setShellType(ShellType.OPENSSL_WINDOWS);
    }

    @Test
    public void execShell() throws IOException {

        ShellExecutor shellExecutor = new WindowsShellExecutor();
        shellExecutor.execute(shellParam);
    }
}
