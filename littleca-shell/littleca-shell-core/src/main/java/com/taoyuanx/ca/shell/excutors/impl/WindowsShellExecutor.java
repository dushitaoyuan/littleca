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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/3/25
 */
public class WindowsShellExecutor implements ShellExecutor {
    static Logger LOG = LoggerFactory.getLogger(WindowsShellExecutor.class);

    @Override
    public void execute(ShellParam shellParam) throws Exception {
        String cmd = shellParam.getShellPath() + " " + buildShellArgs(shellParam);
        LOG.info("exe cmd:[{}]", cmd);
        LOG.debug("exec cmd:[{}]", cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        try {
            int waitFor = process.waitFor();
            String errorMsg = readProcessStream(process.getErrorStream());
            String stdoutMsg = readProcessStream(process.getInputStream());
            LOG.debug("exec result,stdoutMsg=>[{}],errorMsg=>[{}],waitFor=>{}", errorMsg,stdoutMsg,waitFor);
        } finally {
            process.destroy();
        }
    }
}
