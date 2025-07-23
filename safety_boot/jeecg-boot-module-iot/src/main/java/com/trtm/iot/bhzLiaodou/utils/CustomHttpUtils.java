package com.trtm.iot.bhzLiaodou.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lis1
 * @date 2022/10/8
 * @time 15:32
 */
public class CustomHttpUtils {

    /**
     * 获取 post 请求的 byte[] 数组
     *
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }
}
