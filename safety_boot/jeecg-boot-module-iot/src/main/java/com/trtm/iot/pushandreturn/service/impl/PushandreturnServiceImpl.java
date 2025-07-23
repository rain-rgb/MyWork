package com.trtm.iot.pushandreturn.service.impl;

import com.trtm.iot.pushandreturn.entity.Pushandreturn;
import com.trtm.iot.pushandreturn.mapper.PushandreturnMapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 接口推送信息
 * @Author: jeecg-boot
 * @Date:   2023-05-19
 * @Version: V1.0
 */
@Service
public class PushandreturnServiceImpl extends ServiceImpl<PushandreturnMapper, Pushandreturn> implements IPushandreturnService {

    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void saveData(int id, String valueOf, String remark, String body) {
        //保存推送信息
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
            Pushandreturn pushandreturn = new Pushandreturn();
            pushandreturn.setPushdataid(String.valueOf(id));
            pushandreturn.setPushjson(String.valueOf(valueOf));
            pushandreturn.setPushname(remark);
            pushandreturn.setReturnvalue(body);
            pushandreturn.setPushdate(new Date());
            pushandreturnService.save(pushandreturn);
        } catch (Exception e) {

        }
    }
}
