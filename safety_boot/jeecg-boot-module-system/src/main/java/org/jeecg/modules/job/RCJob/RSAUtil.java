package org.jeecg.modules.job.RCJob;

import cn.hutool.json.JSONObject;
import com.trtm.iot.achievementslist.entity.AchievementsList;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
//import org.bouncycastle.jcajce.provider.digest.MD5;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.math.BigInteger;
import java.security.*;

import java.security.spec.*;
import java.util.Base64;

@RestController
@RequestMapping("/rcjob/rsautil")
public class RSAUtil {
    private static final String ALG_RSA = "RSA";
    private static final String ALG_DSA = "DSA";

    public static String encrypt(String data, String publicKey) {
        String encryptData = null;
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
//            // 不加上这行java.security.Security.addProvider ,就会报java.security.InvalidKeyException: IOException : Short read of DER length 错误
            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            PublicKey pubKey = keyFac.generatePublic(new X509EncodedKeySpec(hexString2ByteArr(publicKey)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] result = cipher(data.getBytes("UTF-8"), cipher, getBlockSize(pubKey) - 11);
            encryptData = byteArr2HexString(result);
        } catch (Exception e) {
            e.printStackTrace();
            encryptData = null;
        }
        return encryptData;
    }

    private static byte[] cipher(byte[] data, Cipher cipher, int blockSize) throws Exception {
        final ByteArrayInputStream in = new ByteArrayInputStream(data);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] cache = new byte[blockSize];
        while (true) {
            final int r = in.read(cache);
            if (r < 0) {
                break;
            }
            final byte[] temp = cipher.doFinal(cache, 0, r);
            out.write(temp, 0, temp.length);
        }
        return out.toByteArray();
    }

    public static byte[] hexString2ByteArr(String hexString) {
        if ((hexString == null) || (hexString.length() % 2 != 0)) {
            return new byte[0];
        }
        byte[] dest = new byte[hexString.length() / 2];
        for (int i = 0;
             i < dest.length;
             i++) {
            String val = hexString.substring(2 * i, 2 * i + 2);
            dest[i] = ((byte) Integer.parseInt(val, 16));
        }
        return dest;
    }

    private static int getBlockSize(final Key key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String alg = key.getAlgorithm();
        final KeyFactory keyFactory = KeyFactory.getInstance(alg);
        if (key instanceof PublicKey) {
            final BigInteger prime;
            if (ALG_RSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, RSAPublicKeySpec.class).getModulus();
            } else if (ALG_DSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, DSAPublicKeySpec.class).getP();
            } else {
                throw new NoSuchAlgorithmException("不支持的解密算法：" + alg);
            }
            return prime.toString(2).length() / 8;

        } else if (key instanceof PrivateKey) {
            final BigInteger prime;
            if (ALG_RSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, RSAPrivateKeySpec.class).getModulus();
            } else if (ALG_DSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, DSAPrivateKeySpec.class).getP();
            } else {
                throw new NoSuchAlgorithmException("不支持的解密算法：" + alg);
            }
            return prime.toString(2).length() / 8;
        } else {
            throw new RuntimeException("不支持的密钥类型：" + key.getClass());
        }
    }

    public static String byteArr2HexString(byte[] bytearr) {
        if (bytearr == null) {
            return "null";
        }
        StringBuffer sb = new StringBuffer();
        for (int k = 0;
             k < bytearr.length;
             k++) {
            if ((bytearr[k] & 0xFF) < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(bytearr[k] & 0xFF, 16));
        }
        return sb.toString();
    }

    public static String main() {
        String publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100de4e2212f9e69e2a71028b69c7928b414829e26f5bacfc23b86b9c53ecdeeeea273b9d5fd98e9a31cb0b57adff08792cbec8f51ea7201114b6fbd5fa8c04ff6dcfc4e9f51479e5a0994da7d253d51ddc6039a9d93ece344c45f3b87dc25c630e346e5e884bc2e067b7e5bdb67416149394316ce8520671a513202e6bb67a72290203010001";
        // 使用公钥得到加密数据
        String encryptStr = RSAUtil.encrypt("Cico@123", publicKey);
        System.out.println(encryptStr);
        return encryptStr;
    }


    @GetMapping(value = "/getStr")
    public Result<?> queryById(String psw) {
        String publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100de4e2212f9e69e2a71028b69c7928b414829e26f5bacfc23b86b9c53ecdeeeea273b9d5fd98e9a31cb0b57adff08792cbec8f51ea7201114b6fbd5fa8c04ff6dcfc4e9f51479e5a0994da7d253d51ddc6039a9d93ece344c45f3b87dc25c630e346e5e884bc2e067b7e5bdb67416149394316ce8520671a513202e6bb67a72290203010001";
        // 使用公钥得到加密数据
        String encryptStr = "";
        if (StringUtils.isNotBlank(psw)) {
            encryptStr = RSAUtil.encrypt(psw, publicKey);
        } else {
            encryptStr = RSAUtil.encrypt("Cico@123", publicKey);
        }

        System.out.println(encryptStr);
        return Result.OK(encryptStr);
    }

    public static String queryById22(String psw) {
        String publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100de4e2212f9e69e2a71028b69c7928b414829e26f5bacfc23b86b9c53ecdeeeea273b9d5fd98e9a31cb0b57adff08792cbec8f51ea7201114b6fbd5fa8c04ff6dcfc4e9f51479e5a0994da7d253d51ddc6039a9d93ece344c45f3b87dc25c630e346e5e884bc2e067b7e5bdb67416149394316ce8520671a513202e6bb67a72290203010001";
        // 使用公钥得到加密数据
        String encryptStr = "";
        if (StringUtils.isNotBlank(psw)) {
            encryptStr = RSAUtil.encrypt(psw, publicKey);
        } else {
            encryptStr = RSAUtil.encrypt("Cico@123", publicKey);
        }

        System.out.println(encryptStr);
        return encryptStr;
    }

    // 开化沥青加密
      public static String queryByIdkhlq()  {

        cn.hutool.json.JSONObject postobj=new cn.hutool.json.JSONObject();
        postobj.set("account","kaihuaChengming");
        postobj.set("passWord","khcm113355..");
          String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyHzXZ09W23+XMMTF1q3RjOSWNYVpqTpl20HGeV0pVlot9Wm33n2G2AsJQRQWNn9YOEEHVecJTEP4haOo4eeU3SIcX4cXjlwRvrbL5cXjeQpVuHat/C0rDC7jVoSuJJ5NFQFp1Plbc6AcD0Cb6twMII70O9y8FAnPpNC+xhW+UFQIDAQAB";
        // 使用公钥得到加密数据
          String  encryptStr = "";
          try {
              encryptStr = encryptByPublicKey(postobj.toString(),publicKey);
          } catch (Exception e) {
              throw new RuntimeException(e);
          }

          System.out.println(encryptStr);
        return encryptStr;

    }

    public static String encryptByPublicKey(String data, String publicKeyStr) throws Exception {
        //Java原生base64解码
        byte[] pubKey = Base64.getDecoder().decode(publicKeyStr);
        //创建X509编码密钥规范
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        //返回转换指定算法的KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //根据X509编码密钥规范产生公钥对象
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //用公钥初始化此Cipher对象（加密模式）
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //对数据加密
        byte[] encrypt = cipher.doFinal(data.getBytes());
        //返回base64编码后的字符串
        return Base64.getEncoder().encodeToString(encrypt);
    }

//    @GetMapping(value = "/md5")
//    public String queryMD5(String clientId,String clientSecret,String username) {
//     return   DigestUtils.md5Hex(clientId+clientSecret+username);
//    }

}
