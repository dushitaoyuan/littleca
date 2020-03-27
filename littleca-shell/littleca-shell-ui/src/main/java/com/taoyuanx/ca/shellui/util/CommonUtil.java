package com.taoyuanx.ca.shellui.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.hutool.core.util.ZipUtil;

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