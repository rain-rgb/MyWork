package com.trtm.iot.bhzcailiaocbtj.service.impl;

import com.trtm.iot.bhzcailiaocbtj.entity.BhzCailiaoCbtj;
import com.trtm.iot.bhzcailiaocbtj.mapper.BhzCailiaoCbtjMapper;
import com.trtm.iot.bhzcailiaocbtj.service.IBhzCailiaoCbtjService;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bhz_cailiao_cbtj
 * @Author: jeecg-boot
 * @Date:   2022-12-06
 * @Version: V1.0
 */
@Service
public class BhzCailiaoCbtjServiceImpl extends ServiceImpl<BhzCailiaoCbtjMapper, BhzCailiaoCbtj> implements IBhzCailiaoCbtjService {

    @Autowired
    private  BhzCailiaoCbtjMapper cailiaoCbtjMapper;

    @Override
    public BhzCailiaoCbtj selectclOne(String shebeiNo, Integer materialeType, String materialeName) {
        return cailiaoCbtjMapper.selectclOne(shebeiNo,materialeType,materialeName);
    }
}
