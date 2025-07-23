package com.trtm.iot.wzcailiaonamedictall.service;

import com.trtm.iot.wzcailiaonamedictall.entity.WzcailiaonamedictAll;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: wzcailiaonamedict_all
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
public interface IWzcailiaonamedictAllService extends IService<WzcailiaonamedictAll> {

    WzcailiaonamedictAll getcailiaoInfo(String cailiaonno);

}
