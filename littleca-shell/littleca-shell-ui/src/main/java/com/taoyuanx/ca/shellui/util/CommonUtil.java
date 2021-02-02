package com.taoyuanx.ca.shellui.util;

import java.io.File;

public class CommonUtil {

    public static  String forceBuildPath(String path) {
        File absFile = new File(path);
        if (absFile.isDirectory()) {
            if (!absFile.exists()) {
                absFile.mkdirs();
            }
        } else {
            if (!absFile.getParentFile().exists()) {
                absFile.getParentFile().mkdirs();
            }
        }
        return absFile.getAbsolutePath();
    }


}