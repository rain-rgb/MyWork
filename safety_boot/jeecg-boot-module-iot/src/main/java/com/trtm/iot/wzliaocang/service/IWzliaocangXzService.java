package com.trtm.iot.wzliaocang.service;

import com.trtm.iot.wzliaocang.entity.WzliaocangXz;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: wzliaocang_xz
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
public interface IWzliaocangXzService extends IService<WzliaocangXz> {

    void add(WzliaocangXz wzliaocangXz);
}
