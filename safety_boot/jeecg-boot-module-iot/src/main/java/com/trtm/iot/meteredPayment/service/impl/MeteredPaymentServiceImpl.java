package com.trtm.iot.meteredPayment.service.impl;

import com.trtm.iot.meteredPayment.entity.MeteredPayment;
import com.trtm.iot.meteredPayment.mapper.MeteredPaymentMapper;
import com.trtm.iot.meteredPayment.service.IMeteredPaymentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 计量支付
 * @Author: jeecg-boot
 * @Date:   2022-07-14
 * @Version: V1.0
 */
@Service
public class MeteredPaymentServiceImpl extends ServiceImpl<MeteredPaymentMapper, MeteredPayment> implements IMeteredPaymentService {

}
