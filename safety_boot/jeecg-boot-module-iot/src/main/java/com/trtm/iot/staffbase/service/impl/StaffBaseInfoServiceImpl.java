package com.trtm.iot.staffbase.service.impl;

import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.trtm.iot.staffbase.mapper.StaffBaseInfoMapper;
import com.trtm.iot.staffbase.mapper.StaffWorkDetailMapper;
import com.trtm.iot.staffbase.service.IStaffBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: staff_base_info
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
@Service
public class StaffBaseInfoServiceImpl extends ServiceImpl<StaffBaseInfoMapper, StaffBaseInfo> implements IStaffBaseInfoService {

    @Autowired
    private StaffBaseInfoMapper staffBaseInfoMapper;
    @Override
    public List<StaffBaseInfo> getPushList(String renyuantype,String orgcode) {
        return staffBaseInfoMapper.getPushList(renyuantype, orgcode);
    }

    @Override
    public List<String> userIdList(String orgcode) {
        return staffBaseInfoMapper.userIdList(orgcode);
    }
}
