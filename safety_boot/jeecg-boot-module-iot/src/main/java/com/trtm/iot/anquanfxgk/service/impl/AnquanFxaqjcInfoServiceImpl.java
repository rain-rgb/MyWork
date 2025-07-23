package com.trtm.iot.anquanfxgk.service.impl;

import com.trtm.iot.anquanfxgk.entity.AnquanFxaqjcInfo;
import com.trtm.iot.anquanfxgk.mapper.AnquanFxaqjcInfoMapper;
import com.trtm.iot.anquanfxgk.service.IAnquanFxaqjcInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: anquan_fxaqjc_info
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Service
public class AnquanFxaqjcInfoServiceImpl extends ServiceImpl<AnquanFxaqjcInfoMapper, AnquanFxaqjcInfo> implements IAnquanFxaqjcInfoService {

    @Autowired
    private AnquanFxaqjcInfoMapper fxaqjcInfoMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer modifyFxaqjcTypeByZhenggaiTime() {
        return fxaqjcInfoMapper.modifyFxaqjcTypeByZhenggaiTime();
    }
}
