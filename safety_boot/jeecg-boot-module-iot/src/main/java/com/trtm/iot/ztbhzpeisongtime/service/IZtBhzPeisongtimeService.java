package com.trtm.iot.ztbhzpeisongtime.service;

import com.trtm.iot.ztbhzpeisongtime.entity.ZtBhzPeisongtime;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 自建拌和站配送考核标准时间表
 * @Author: jeecg-boot
 * @Date:   2023-11-01
 * @Version: V1.0
 */
public interface IZtBhzPeisongtimeService extends IService<ZtBhzPeisongtime> {

    ZtBhzPeisongtime selecdw(String projname);

    ZtBhzPeisongtime ztyjsb(String projname);
}
