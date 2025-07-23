package com.trtm.sy.registerfile.tool;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @since
 */
public class ServletInfoUtil {

    public static Map<String, String> getServletInfo(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();
        String backgroundPath = scheme + "://" + serverName + ":" + port + contextPath;// 后台路径
        String frontdeskPath = scheme + "://" + serverName + ":" + port;// 前台路径

        Map<String, String> map = new HashMap<>();
        map.put("backgroundPath", backgroundPath);
        map.put("frontdeskPath", frontdeskPath);

        return map;
    }
}
