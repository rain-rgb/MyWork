package com.trtm.iot.pipepileYujing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.pipepileYujing.entity.PipepileYujing;
import com.trtm.iot.pipepileYujing.mapper.PipepileYujingMapper;
import com.trtm.iot.pipepileYujing.service.IPipepileYujingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 管桩预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-09-15
 * @Version: V1.0
 */
@Service
public class PipepileYujingServiceImpl extends ServiceImpl<PipepileYujingMapper, PipepileYujing> implements IPipepileYujingService {

    @Override
    public PipepileYujing selectones(String shebeino) {
        try {
            QueryWrapper<PipepileYujing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebei_no",shebeino);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
