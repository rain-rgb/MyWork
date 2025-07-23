package com.trtm.iot.shebeiWarncfg.service.impl;

import com.trtm.iot.shebeiWarncfg.entity.ShebeiWarncfg;
import com.trtm.iot.shebeiWarncfg.mapper.ShebeiWarncfgMapper;
import com.trtm.iot.shebeiWarncfg.service.IShebeiWarncfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 设备状态预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-08-04
 * @Version: V1.0
 */
@Service
public class ShebeiWarncfgServiceImpl extends ServiceImpl<ShebeiWarncfgMapper, ShebeiWarncfg> implements IShebeiWarncfgService {

    @Autowired ShebeiWarncfgMapper shebeiWarncfgMapper;
    @Override
    public ShebeiWarncfg getcfgdata(String sbjno) {
        return shebeiWarncfgMapper.getcfgdata(sbjno);
    }
}
