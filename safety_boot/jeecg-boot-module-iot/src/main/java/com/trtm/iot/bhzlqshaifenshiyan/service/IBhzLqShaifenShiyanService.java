package com.trtm.iot.bhzlqshaifenshiyan.service;

import com.trtm.iot.bhzlqshaifenshiyan.entity.BhzLqShaifenShiyan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 沥青筛分试验数据信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
public interface IBhzLqShaifenShiyanService extends IService<BhzLqShaifenShiyan> {

    BhzLqShaifenShiyan selectone(String shebeibianhao, Integer cailiaoid, String uuid);
}
