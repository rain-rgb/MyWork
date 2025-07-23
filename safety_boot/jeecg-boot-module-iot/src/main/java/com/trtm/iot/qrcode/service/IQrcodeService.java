package com.trtm.iot.qrcode.service;

import com.trtm.iot.qrcode.entity.Qrcode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 二维码数据表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
public interface IQrcodeService extends IService<Qrcode> {

    Qrcode queryGetOne(String uuid);

    Qrcode getQRlistByidN( Integer id,Integer maxn ,Integer minin);

}
