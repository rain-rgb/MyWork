package com.trtm.iot.beamorder.service.impl;

import com.trtm.iot.beamorder.entity.BeamOrder;
import com.trtm.iot.beamorder.mapper.BeamOrderMapper;
import com.trtm.iot.beamorder.service.IBeamOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 粱订单信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-25
 * @Version: V1.0
 */
@Service
public class BeamOrderServiceImpl extends ServiceImpl<BeamOrderMapper, BeamOrder> implements IBeamOrderService {

}
