package com.taoyuanx.ca.shell.excutors;


import com.taoyuanx.ca.shell.params.ShellParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/3/25
 */
public interface ShellExecutor {
    public static final String EMPTY_ARG_FLAG = "1";

    void execute(ShellParam shellParam) throws Exception;

    default String buildShellArgs(ShellParam shellParam) {
        List<String> shellParams = shellParam.buildShellParams();
        return shellParams.stream().map(str -> {
            if (Objects.isNull(str) || str.isEmpty()) {
                return EMPTY_ARG_FLAG;
            }
            return str;
        }).collect(Collectors.joining(" "));
    }

    default String readProcessStream(InputStream inputStream) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        StringBuilder buf = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            buf.append(line);
        }
        br.close();
        return buf.toString();
    }
}
