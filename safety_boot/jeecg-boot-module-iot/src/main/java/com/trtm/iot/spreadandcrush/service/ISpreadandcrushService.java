package com.trtm.iot.spreadandcrush.service;

import com.trtm.iot.spreadandcrush.entity.Spreadandcrush;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 摊铺碾压
 * @Author: jeecg-boot
 * @Date:   2023-04-20
 * @Version: V1.0
 */
public interface ISpreadandcrushService extends IService<Spreadandcrush> {

    List<Spreadandcrush> getList(String Shebeino,Integer id);
}
