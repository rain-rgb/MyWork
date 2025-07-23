package com.trtm.iot.wzgongyingshangman.service;

import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: wzgongyingshang_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
public interface IWzgongyingshangManService extends IService<WzgongyingshangMan> {

    Map selectqueryone(String sysOrgCode);

    WzgongyingshangMan selectnameone(String gongyingshangdanweibianma);

    WzgongyingshangMan selectnameone1(String gongyinsgangNo);
}
