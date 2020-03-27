package com.taoyuanx.ca.shell.excutors;


import com.taoyuanx.ca.shell.params.ShellParam;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/3/25
 */
public interface ShellExecutor {
    public static final String EMPTY_ARG_FLAG = "1";

    void execute(ShellParam shellParam) throws IOException;

    default String buildShellArgs(ShellParam shellParam) {
        List<String> shellParams = shellParam.buildShellParams();
        return shellParams.stream().map(str -> {
            if (Objects.isNull(str) || str.isEmpty()) {
                return EMPTY_ARG_FLAG;
            }
            return str;
        }).collect(Collectors.joining(" "));
    }
}
