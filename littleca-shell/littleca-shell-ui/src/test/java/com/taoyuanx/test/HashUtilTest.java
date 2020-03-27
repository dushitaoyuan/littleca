package com.taoyuanx.test;

import cn.hutool.core.util.HashUtil;
import org.junit.Test;

/**
 * @author dushitaoyuan
 * @desc 用途描述
 * @date 2020/3/26
 */
public class HashUtilTest {
    @Test
    public  void test(){
        System.out.println(HashUtil.fnvHash("1234"));
        System.out.println(HashUtil.fnvHash("1234"));

        System.out.println(Math.abs(HashUtil.fnvHash("12345")));

    }
}
