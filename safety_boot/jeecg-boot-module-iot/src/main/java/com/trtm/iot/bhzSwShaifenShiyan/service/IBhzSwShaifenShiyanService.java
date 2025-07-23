package com.trtm.iot.bhzSwShaifenShiyan.service;

import com.trtm.iot.bhzSwShaifenShiyan.entity.BhzSwShaifenShiyan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bhz_sw_shaifen_shiyan
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
public interface IBhzSwShaifenShiyanService extends IService<BhzSwShaifenShiyan> {

    BhzSwShaifenShiyan selectone(String shebeibianhao, Integer cailiaoid, String uuid);
}
