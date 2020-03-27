package com.taoyuanx.ca.shell.excutors.impl;

import com.taoyuanx.ca.shell.excutors.ShellExecutor;
import com.taoyuanx.ca.shell.params.ShellParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/3/25
 */
public class LinuxShellExecutor implements ShellExecutor {
    static Logger LOG = LoggerFactory.getLogger(LinuxShellExecutor.class);

    @Override
    public void execute(ShellParam shellParam) throws IOException {
        String cmd = shellParam.getShellPath() + " " + buildShellArgs(shellParam);
        LOG.info("exe cmd:[{}]", cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
        StringBuilder buf = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            buf.append(line);
        }
        LOG.info("exe info:{}", buf);
        process.destroy();
        br.close();
    }
}
