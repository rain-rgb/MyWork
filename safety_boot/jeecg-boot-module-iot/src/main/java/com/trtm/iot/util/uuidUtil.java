package com.trtm.iot.util;

import java.util.UUID;

/**
 * \* @author: Xx
 * \* Date: 2021/4/2
 * \* Time: 14:39
 * \* Description:
 * \
 */
public class uuidUtil {

    /**
     * 32位uuid的生成
     * @return
     */
    public static String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
