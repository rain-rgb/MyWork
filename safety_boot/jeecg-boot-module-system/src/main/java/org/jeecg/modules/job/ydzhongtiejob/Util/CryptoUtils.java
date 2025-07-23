package org.jeecg.modules.job.ydzhongtiejob.Util;

/**
 * @ClassName -> CryptoUtils
 * @Description CryptoUtils 加密
 * @Date 2022/9/30 11:25
 * @Version 1.0
 */

import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持AES、DES、RSA加密、数字签名以及生成对称密钥和非对称密钥对
 */
public class CryptoUtils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    private static final Map<Algorithm, KeyFactory> KEY_FACTORY_CACHE = new ConcurrentHashMap<>();
    private static final Map<Algorithm, Cipher> CIPHER_CACHE = new HashMap<>();


    /**
     * 对称加密
     *
     * @param secretKey 密钥
     * @param iv        加密向量，只有CBC模式才支持
     * @param plainText 明文
     * @param algorithm 对称加密算法，如AES、DES
     */
    public static String encryptSymmetrically(String secretKey, String iv, String plainText, Algorithm algorithm) throws Exception {
        SecretKey key = decodeSymmetricKey(secretKey, algorithm);
        IvParameterSpec ivParameterSpec = StringUtils.isBlank(iv) ? null : decodeIv(iv);
        byte[] plainTextInBytes = plainText.getBytes(DEFAULT_CHARSET);
        byte[] ciphertextInBytes = transform(algorithm, Cipher.ENCRYPT_MODE, key, ivParameterSpec, plainTextInBytes);

        return BASE64_ENCODER.encodeToString(ciphertextInBytes);
    }

    /**
     * 对称解密
     *
     * @param secretKey  密钥
     * @param iv         加密向量，只有CBC模式才支持
     * @param ciphertext 密文
     * @param algorithm  对称加密算法，如AES、DES
     */
    public static String decryptSymmetrically(String secretKey, String iv, String ciphertext, Algorithm algorithm) throws Exception {
        SecretKey key = decodeSymmetricKey(secretKey, algorithm);
        IvParameterSpec ivParameterSpec = StringUtils.isBlank(iv) ? null : decodeIv(iv);
        byte[] ciphertextInBytes = BASE64_DECODER.decode(ciphertext);
        byte[] plainTextInBytes = transform(algorithm, Cipher.DECRYPT_MODE, key, ivParameterSpec, ciphertextInBytes);
        return new String(plainTextInBytes, DEFAULT_CHARSET);
    }

    /**
     * 将密钥进行Base64位解码，重新生成SecretKey实例
     *
     * @param secretKey 密钥
     * @param algorithm 算法
     */
    private static SecretKey decodeSymmetricKey(String secretKey, Algorithm algorithm) {
        byte[] key = BASE64_DECODER.decode(secretKey);
        return new SecretKeySpec(key, algorithm.getName());
    }

    private static IvParameterSpec decodeIv(String iv) {
        byte[] ivInBytes = BASE64_DECODER.decode(iv);
        return new IvParameterSpec(ivInBytes);
    }

    private static byte[] transform(Algorithm algorithm, int mode, Key key, IvParameterSpec iv, byte[] msg) throws Exception {
        Cipher cipher = CIPHER_CACHE.get(algorithm);
        // double check，减少上下文切换
        if (cipher == null) {
            synchronized (CryptoUtils.class) {
                if ((cipher = CIPHER_CACHE.get(algorithm)) == null) {
                    cipher = determineWhichCipherToUse(algorithm);
                    CIPHER_CACHE.put(algorithm, cipher);
                }
                cipher.init(mode, key, iv);
                return cipher.doFinal(msg);
            }
        }

        synchronized (CryptoUtils.class) {
            cipher.init(mode, key, iv);
            return cipher.doFinal(msg);
        }
    }

    private static Cipher determineWhichCipherToUse(Algorithm algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher;
        String transformation = algorithm.getTransformation();
        // 官方推荐的transformation使用algorithm/mode/padding组合，SunJCE使用ECB作为默认模式，使用PKCS5Padding作为默认填充
        if (StringUtils.isNotEmpty(transformation)) {
            cipher = Cipher.getInstance(transformation);
        } else {
            cipher = Cipher.getInstance(algorithm.getName());
        }

        return cipher;
    }

    /**
     * 算法分为加密算法和签名算法，更多算法实现见：<br/>
     * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#impl">jdk8中的标准算法</a>
     */

    public static class Algorithm {

        /**
         * 以下为加密算法，加密算法transformation采用algorithm/mode/padding的形式
         */
        public static final Algorithm AES_ECB_PKCS5 = new Algorithm("AES", "AES/ECB/PKCS5Padding", 128);
        public static final Algorithm AES_CBC_PKCS5 = new Algorithm("AES", "AES/CBC/PKCS5Padding", 128);
        public static final Algorithm DES_ECB_PKCS5 = new Algorithm("DES", "DES/ECB/PKCS5Padding", 56);
        public static final Algorithm DES_CBC_PKCS5 = new Algorithm("DES", "DES/CBC/PKCS5Padding", 56);
        public static final Algorithm RSA_ECB_PKCS1 = new Algorithm("RSA", "RSA/ECB/PKCS1Padding", 1024);

        /**
         * 以下为签名算法
         */
        public static final Algorithm DSA = new Algorithm("DSA", 1024);
        public static final Algorithm SHA1WithDSA = new Algorithm("SHA256WithRSA", 1024);
        public static final Algorithm SHA1WithRSA = new Algorithm("SHA1WithRSA", 2048);
        public static final Algorithm SHA256WithRSA = new Algorithm("SHA256WithRSA", 2048);

        private String name;
        private String transformation;
        private int keySize;

        public Algorithm(String name, int keySize) {
            this(name, null, keySize);
        }
        public Algorithm() {

        }

        public Algorithm(String name, String transformation, int keySize) {
            this.name = name;
            this.transformation = transformation;
            this.keySize = keySize;
        }

        public static Algorithm getAesEcbPkcs5() {
            return AES_ECB_PKCS5;
        }

        public static Algorithm getAesCbcPkcs5() {
            return AES_CBC_PKCS5;
        }

        public static Algorithm getDesEcbPkcs5() {
            return DES_ECB_PKCS5;
        }

        public static Algorithm getDesCbcPkcs5() {
            return DES_CBC_PKCS5;
        }

        public static Algorithm getRsaEcbPkcs1() {
            return RSA_ECB_PKCS1;
        }

        public static Algorithm getDSA() {
            return DSA;
        }

        public static Algorithm getSHA1WithDSA() {
            return SHA1WithDSA;
        }

        public static Algorithm getSHA1WithRSA() {
            return SHA1WithRSA;
        }

        public static Algorithm getSHA256WithRSA() {
            return SHA256WithRSA;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTransformation() {
            return transformation;
        }

        public void setTransformation(String transformation) {
            this.transformation = transformation;
        }

        public int getKeySize() {
            return keySize;
        }

        public void setKeySize(int keySize) {
            this.keySize = keySize;
        }
    }

    public static class AsymmetricKeyPair {

        private String publicKey;
        private String privateKey;

        public AsymmetricKeyPair() {
        }

        public AsymmetricKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }

}


