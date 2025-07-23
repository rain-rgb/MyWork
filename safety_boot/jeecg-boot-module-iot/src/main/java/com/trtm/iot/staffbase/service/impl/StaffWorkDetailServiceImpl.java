package com.trtm.iot.staffbase.service.impl;

import com.trtm.iot.staffbase.entity.StaffWorkDetail;
import com.trtm.iot.staffbase.mapper.StaffWorkDetailMapper;
import com.trtm.iot.staffbase.service.IStaffWorkDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: staff_work_detail
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
@Service
public class StaffWorkDetailServiceImpl extends ServiceImpl<StaffWorkDetailMapper, StaffWorkDetail> implements IStaffWorkDetailService {

    @Autowired
    private StaffWorkDetailMapper staffWorkDetailMapper;
    @Override
    public List<StaffWorkDetail> getPushList(String orgcode) {
        return staffWorkDetailMapper.getPushList(orgcode);
    }
}
