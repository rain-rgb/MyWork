package com.trtm.iot.bhzSupervisionOrder.service;

import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: bhz_supervision_order
 * @Author: jeecg-boot
 * @Date:   2023-02-08
 * @Version: V1.0
 */
public interface IBhzSupervisionOrderService extends IService<BhzSupervisionOrder> {

    Integer selectCountByNo(String num);
}
