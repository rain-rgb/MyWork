package com.trtm.iot.zhiliangqrcode.service;

import com.trtm.iot.zhiliangqrcode.entity.Zhiliangqrcode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: zhiliangqrcode
 * @Author: jeecg-boot
 * @Date:   2022-08-10
 * @Version: V1.0
 */
public interface IZhiliangqrcodeService extends IService<Zhiliangqrcode> {

    Zhiliangqrcode selectOne(String uuid);
}
