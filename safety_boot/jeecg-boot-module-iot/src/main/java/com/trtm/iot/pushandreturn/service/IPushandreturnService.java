package com.trtm.iot.pushandreturn.service;

import com.trtm.iot.pushandreturn.entity.Pushandreturn;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 接口推送信息
 * @Author: jeecg-boot
 * @Date:   2023-05-19
 * @Version: V1.0
 */
public interface IPushandreturnService extends IService<Pushandreturn> {

    void saveData(int id, String valueOf, String remark, String body);
}
