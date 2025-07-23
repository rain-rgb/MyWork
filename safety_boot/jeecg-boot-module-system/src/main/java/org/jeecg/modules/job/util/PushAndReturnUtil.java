package org.jeecg.modules.job.util;

import com.trtm.iot.pushandreturn.entity.Pushandreturn;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName PushAndReturn：
 * @Description 数据推送信息
 * @Author 55314
 * @Date 2023/5/19 11:25
 * @Version 1.0
 **/

public class PushAndReturnUtil {

    @Autowired
    private IPushandreturnService pushandreturnService;

    public void saveData(Integer id,String Remark, String JsonObject, String back){

    }
}
