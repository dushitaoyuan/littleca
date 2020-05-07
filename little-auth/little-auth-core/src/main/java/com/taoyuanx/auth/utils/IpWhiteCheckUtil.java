package com.taoyuanx.auth.utils;

import java.util.*;
import java.util.regex.Pattern;

public class IpWhiteCheckUtil {
    // IP的正则
    private static Pattern pattern = Pattern
            .compile("(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
                    + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
                    + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
                    + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})");

    /**
     * getAvaliIpList:(根据IP白名单设置获取可用的IP列表).
     *
     * @return
     * @date 2017-4-17 下午02:50:20
     */

    private static Set<String> getAvaliIpList(String allowIp) {

        Set<String> ipList = new HashSet<String>();
        String[] splitRex = allowIp.replaceAll("\\s", "").split(";");//拆分出白名单正则
        for (String allow : splitRex) {
            if (allow.indexOf("*") > -1) {//处理通配符 *
                String[] ips = allow.split("\\.");
                String[] from = new String[]{"0", "0", "0", "0"};
                String[] end = new String[]{"255", "255", "255", "255"};
                List<String> tem = new ArrayList<String>();
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i].indexOf("*") > -1) {
                        tem = complete(ips[i]);
                        from[i] = null;
                        end[i] = null;
                    } else {
                        from[i] = ips[i];
                        end[i] = ips[i];
                    }

                }

                StringBuilder fromIP = new StringBuilder();
                StringBuilder endIP = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    if (from[i] != null) {
                        fromIP.append(from[i]).append(".");
                        endIP.append(end[i]).append(".");
                    } else {
                        fromIP.append("[*].");
                        endIP.append("[*].");
                    }
                }
                fromIP.deleteCharAt(fromIP.length() - 1);
                endIP.deleteCharAt(endIP.length() - 1);

                for (String s : tem) {
                    String ip = fromIP.toString().replace("[*]",
                            s.split(";")[0])
                            + "-"
                            + endIP.toString().replace("[*]", s.split(";")[1]);
                    if (validate(ip)) {
                        ipList.add(ip);
                    }
                }
            } else if (allow.contains("/")) {//处理 网段 xxx.xxx.xxx./24
                ipList.add(allow);
            } else {//处理单个 ip 或者 范围
                if (validate(allow)) {
                    ipList.add(allow);
                }
            }

        }

        return ipList;
    }

    /**
     * 对单个IP节点进行范围限定
     *
     * @param arg
     * @return 返回限定后的IP范围，格式为List[10;19, 100;199]
     */
    private static List<String> complete(String arg) {
        List<String> com = new ArrayList<String>();
        if (arg.length() == 1) {
            com.add("0;255");
        } else if (arg.length() == 2) {
            String s1 = complete(arg, 1);
            if (s1 != null) {
                com.add(s1);
            }
            String s2 = complete(arg, 2);
            if (s2 != null) {
                com.add(s2);

            }
        } else {
            String s1 = complete(arg, 1);
            if (s1 != null) {
                com.add(s1);
            }
        }
        return com;
    }

    private static String complete(String arg, int length) {
        String from = "";
        String end = "";
        if (length == 1) {
            from = arg.replace("*", "0");
            end = arg.replace("*", "9");
        } else {
            from = arg.replace("*", "00");
            end = arg.replace("*", "99");
        }
        if (Integer.valueOf(from) > 255) {
            return null;
        }
        if (Integer.valueOf(end) > 255) {
            end = "255";
        }
        return from + ";" + end;
    }

    /**
     * 在添加至白名单时进行格式校验
     *
     * @param ip
     * @return
     */
    private static boolean validate(String ip) {
        for (String s : ip.split("-")) {
            if (!pattern.matcher(s).matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * checkWhiteIP:(根据IP,及可用Ip列表来判断ip是否包含在白名单之中).
     *
     * @param ip
     * @param ipList
     * @return
     * @date 2017-4-17 下午03:01:03
     */
    private static boolean checkWhiteIP(String ip, Set<String> ipList) {
        if (ipList.isEmpty() || ipList.contains(ip)) {
            return true;
        } else {
            for (String allow : ipList) {
                if (allow.indexOf("-") > -1) {
                    String[] from = allow.split("-")[0].split("\\.");
                    String[] end = allow.split("-")[1].split("\\.");
                    String[] tag = ip.split("\\.");

                    // 对IP从左到右进行逐段匹配
                    boolean check = true;
                    for (int i = 0; i < 4; i++) {
                        int s = Integer.valueOf(from[i]);
                        int t = Integer.valueOf(tag[i]);
                        int e = Integer.valueOf(end[i]);
                        if (!(s <= t && t <= e)) {
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        return true;
                    }
                } else if (allow.contains("/")) {//处理 网段 xxx.xxx.xxx./24
                    int splitIndex = allow.indexOf("/");
                    //取出子网段
                    String ipSegment = allow.substring(0, splitIndex); //192.168.3.0
                    //子网数
                    String netmask = allow.substring(splitIndex + 1);//24
                    //ip 转二进制
                    long ipLong = ipToLong(ip);
                    //子网二进制
                    long maskLong = (2L << 32 - 1) - (2L << Integer.valueOf(32 - Integer.valueOf(netmask)) - 1);
                    //ip与和子网相与 得到 网络地址
                    String calcSegment = longToIP(ipLong & maskLong);
                    //如果计算得出网络地址和库中网络地址相同 则合法
                    if (ipSegment.equals(calcSegment)) {
                        return true;

                    }
                }
            }
        }
        return false;
    }

    /**
     * 表示禁止所有ip:(根据IP地址，及IP白名单设置规则判断IP是否包含在白名单).
     *
     * @param ip
     * @param ipWhiteConfig
     * @return
     * @date 2017-4-17 下午03:01:37
     */
    public static boolean checkWhiteIP(String ip, String ipWhiteConfig) {
        //null 白名单为*，表示允许所有ip
        if (Objects.isNull(ipWhiteConfig) || ipWhiteConfig.isEmpty() || "*".equals(ipWhiteConfig)) {
            return true;
        }
        //0 表示禁止所有ip
        if ("0".equals(ipWhiteConfig)) {
            return false;
        }
        Set<String> ipList = getAvaliIpList(ipWhiteConfig);
        return checkWhiteIP(ip, ipList);
    }

    public static long ipToLong(String strIP) {
        long[] ip = new long[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIP.indexOf(".");
        int position2 = strIP.indexOf(".", position1 + 1);
        int position3 = strIP.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIP.substring(0, position1));
        ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIP.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    // 将10进制整数形式转换成127.0.0.1形式的IP地址
    public static String longToIP(long longIP) {
        StringBuilder sb = new StringBuilder("");
        // 直接右移24位
        sb.append(String.valueOf(longIP >>> 24));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longIP & 0x000000FF));
        return sb.toString();
    }


}