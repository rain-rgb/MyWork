package com.trtm.iot.largesbconfigure.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.trtm.iot.largesbconfigure.mapper.LargesbConfigureMapper;
import com.trtm.iot.largesbconfigure.service.ILargesbConfigureService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: largesb_configure
 * @Author: jeecg-boot
 * @Date: 2023-09-12
 * @Version: V1.0
 */
@Service
public class LargesbConfigureServiceImpl extends ServiceImpl<LargesbConfigureMapper, LargesbConfigure> implements ILargesbConfigureService {

    @Autowired
    private LargesbConfigureMapper largesbConfigureMapper;
    @Autowired
    private ShebeiInfoMapper shebeiInfoMapper;

    @Override
    public void saveDoorSbj(String sysOrgCode, String shebeiNo, String doorSbjno) {
        String oneSb = "";
        String twoSb = "";
        ShebeiInfo shebeiOne = shebeiInfoMapper.selectshebeione(shebeiNo);
        if (ObjectUtil.isNotEmpty(shebeiOne)) {
            oneSb = shebeiOne.getSbname();
        }
        ShebeiInfo shebeiTwo = shebeiInfoMapper.selectshebeione(doorSbjno);
        if (ObjectUtil.isNotEmpty(shebeiTwo)) {
            twoSb = shebeiTwo.getSbname();
        }
        largesbConfigureMapper.saveDoorSbj(sysOrgCode, oneSb, shebeiNo, twoSb, doorSbjno);
    }
}
