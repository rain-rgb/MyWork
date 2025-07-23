package org.jeecg.modules.system.test;


import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sun.misc.BASE64Decoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSIpm {

    public static void main(String[] args) {
        String str = "452ahaa48146333";
        String[] split = str.split(";");
        int length = split.length;
        System.out.println(length);
        for (String s : split) {
            System.out.println(s);
        }
        char targetChar = ';';
        long count = str.chars().filter(c -> c == targetChar).count();
        System.out.println("字符 '" + targetChar + "' 出现的次数是: " + count);
    }

    @Test
    public  void Test(){

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("shebeino","2024032003");
        jsonObject1.set("pile_no","cs12345678");
        jsonObject1.set("pile_mileage","LK0+920-LK0+960");
        jsonObject1.set("part_pilec","1");
        jsonObject1.set("part_dep","0.25");
        jsonObject1.set("part_beton","62.74");
        jsonObject1.set("part_speed","0.86");
        jsonObject1.set("part_time","97");
        jsonObject1.set("part_x","0.65");
        jsonObject1.set("part_y","0.74");
        jsonObject1.set("part_state","0");
        jsonObject1.set("part_endtime","1715237046453");
        jsonObject1.set("gzcount","1");
        jsonObject1.set("part_electricity","25.53");

        System.out.println(jsonObject1);
        String s = AESEncode("JVFRSzA+khqg+If8iG75dA==", String.valueOf(jsonObject1));

        System.out.println(s);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("datatype",87);
        jsonObject.set("datavalue",s);
        jsonObject.set("hwid","123456");
        jsonObject.set("hwno","2024032003");
        getCode(jsonObject);
    }
    public static int getCode(JSONObject sendObject) {
        String body = HttpRequest.post("http://47.97.158.215:8036/rest/txd")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        return (Integer) jsonObject1.get("code");
    }
//    java aes 加密函数
    public static String AESEncode(String skey,String content){
        try {
            byte [] raw= Base64.decode(skey);
            SecretKey key=new SecretKeySpec(raw, "AES");
            Cipher cipher= Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] byte_encode=content.getBytes("utf-8");
            byte [] byte_AES=cipher.doFinal(byte_encode);
            String AES_encode=new String(Base64.encode(byte_AES));
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
