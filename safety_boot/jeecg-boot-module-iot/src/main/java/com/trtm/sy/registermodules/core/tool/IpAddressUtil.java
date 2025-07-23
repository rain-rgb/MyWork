package com.trtm.sy.registermodules.core.tool;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wh
 * @Description
 * @program core
 * @Create by H
 */
public class IpAddressUtil {

    private static final String LOCAL_IP = "127.0.0.1";

    private static final String LOCAL_REMOTE_HOST = "0:0:0:0:0:0:0:1";

    /**
     * 获取客户端ip
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            return "-";
        } else {
            String remoteHost = ServletUtil.getClientIP(request);
            return LOCAL_REMOTE_HOST.equals(remoteHost) ? LOCAL_IP : remoteHost;
        }
    }

    /**
     * 获取服务器端离职ip
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getAddress(HttpServletRequest request) {
        String resultJson = "-";

        String ip = getClientIp(request);

        if (ObjectUtil.isEmpty(ip) || NetUtil.isInnerIP(ip)) {
            return resultJson;
        }
        try {
            //TODO 获取阿里云定位api接口
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = "-";
        }
        return resultJson;
    }

}
