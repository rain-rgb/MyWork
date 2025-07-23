package org.jeecg.modules.job.lab.push.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Md5加密算法 品茗提供
 *
 * @author lis1
 * @date 2022/12/9
 * @time 13:33
 */
public class PMUtil {

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String DT_LONG = "yyyyMMddHHmmss";

    /**
     * 当前时间，格式 yyyyMMddHHmmss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String getData() {
        return new SimpleDateFormat(DT_LONG).format(new Date());
    }

    /**
     * AES算法加密文本
     *
     * @param secretKey      密钥
     * @param originalString 需要加密的文本
     * @return 加密的文本(Base64格式)
     **/
    public static String aesEncrypt(String secretKey, String originalString) {
        if (StringUtils.isEmpty(secretKey))
            throw new IllegalArgumentException("this secretKey must not be empty");
        if (StringUtils.isEmpty(originalString))
            throw new IllegalArgumentException("this originalString must not be empty");

        final byte[] byteKey = toByteArray(secretKey);
        SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] originalBytes = originalString.trim().getBytes("UTF-8");
            if (originalBytes.length % 16 != 0) {
                byte[] newOriginalBytes = new byte[(originalBytes.length / 16 + 1) * 16];
                System.arraycopy(originalBytes, 0, newOriginalBytes, 0, originalBytes.length);
                originalBytes = newOriginalBytes;
            }

            final byte[] encryptedBytes = cipher.doFinal(originalBytes);
            return new String(Base64.encodeBase64(encryptedBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(secretKey + " is illegal," + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("unable to encrypt '" + originalString + "'," + e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
        if (org.apache.commons.lang.StringUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /**
     * 字符串编码为UTF-8
     *
     * @param str
     * @return
     */
    public static final String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * MD5算法加密字符串
     *
     * @param originalString 需要加密的字符串
     * @return 加密之后的字符串(16进制格式)
     **/
    public static String md5Encrypt(String originalString) {
        if (org.apache.commons.lang.StringUtils.isEmpty(originalString))
            throw new IllegalArgumentException("this originalString must not be empty");

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(originalString.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final byte[] encrypted = messageDigest.digest();
        return toHexString(encrypted);
    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 需要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }
}
