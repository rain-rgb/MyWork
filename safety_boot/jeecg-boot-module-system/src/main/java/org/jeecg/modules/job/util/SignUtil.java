package org.jeecg.modules.job.util;

/**
 * @ClassName SignUtil：
 * @Description TODO
 * @Author 55314
 * @Date 2021/12/1 13:14
 * @Version 1.0
 **/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 签名生成方法
 * @param appsecret 应用密钥
 * @param x_rio_seq 随机串
 * @param timestamp 时间戳
 * @return
 */

public class SignUtil {
    public static String signature(String appsecret, String x_rio_seq, String timestamp) {
        String sign_str = timestamp + appsecret + x_rio_seq + timestamp;

        return SHA(sign_str, "SHA-256");
    }

    /**
     * 字符串 SHA 加密
     *
     * @param strSourceText
     * @return
     */
    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            // SHA 加密开始
            // 创建加密对象 并傳入加密類型
            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance(strType);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            // 传入要加密的字符串
            messageDigest.update(strText.getBytes());
            // 得到 byte 類型结果
            byte byteBuffer[] = messageDigest.digest();

            // 將 byte 轉換爲 string
            StringBuffer strHexString = new StringBuffer();
            // 遍歷 byte buffer
            for (int i = 0; i < byteBuffer.length; i++) {
                String hex = Integer.toHexString(0xff & byteBuffer[i]);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            // 得到返回結果
            strResult = strHexString.toString();
        }

        return strResult;
    }

}
