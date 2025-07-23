package com.trtm.sy.registermodules.core.tool;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * @author wh
 * @Description 用户代理工具类
 * @program core
 * @Create by H
 */
public class UaUtil {


    private static final String DASH = "-";

    /**
     * 获取客户端浏览器
     *
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        if (ObjectUtil.isEmpty(userAgent)) {
            return DASH;
        } else {
            String browser = userAgent.getBrowser().toString();
            return "Unknown".equals(browser) ? DASH : browser;
        }
    }

    /**
     * 获取客户端操作系统
     *
     * @param request
     * @return
     */
    public static String getOs(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        if (ObjectUtil.isEmpty(userAgent)) {
            return DASH;
        } else {
            String os = userAgent.getOs().toString();
            return "Unknown".equals(os) ? DASH : os;
        }
    }

    /**
     * 获取请求代理头
     *
     * @param request
     * @return
     */
    private static UserAgent getUserAgent(HttpServletRequest request) {
        String userAgentStr = ServletUtil.getHeaderIgnoreCase(request, DASH);
        String requestHeader = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgentUtil.parse(requestHeader);
        if (ObjectUtil.isNotEmpty(userAgentStr)) {
            if ("Unknown".equals(userAgent.getBrowser().getName())) {
                userAgent.setBrowser(new Browser(userAgentStr, null, ""));
            }
        }
        return userAgent;
    }


}
