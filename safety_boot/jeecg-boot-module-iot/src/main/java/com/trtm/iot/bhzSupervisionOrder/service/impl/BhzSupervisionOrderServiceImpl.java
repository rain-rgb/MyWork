package com.trtm.iot.bhzSupervisionOrder.service.impl;

import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.trtm.iot.bhzSupervisionOrder.mapper.BhzSupervisionOrderMapper;
import com.trtm.iot.bhzSupervisionOrder.service.IBhzSupervisionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: bhz_supervision_order
 * @Author: jeecg-boot
 * @Date:   2023-02-08
 * @Version: V1.0
 */
@Service
public class BhzSupervisionOrderServiceImpl extends ServiceImpl<BhzSupervisionOrderMapper, BhzSupervisionOrder> implements IBhzSupervisionOrderService {

    @Autowired
    private BhzSupervisionOrderMapper bhzSupervisionOrderMapper;


    @Override
    public Integer selectCountByNo(String num) {
        return bhzSupervisionOrderMapper.selectCountByNo(num);
    }
}
