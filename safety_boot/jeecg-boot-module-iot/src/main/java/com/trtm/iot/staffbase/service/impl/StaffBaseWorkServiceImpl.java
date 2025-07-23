package com.trtm.iot.staffbase.service.impl;

import com.trtm.iot.staffbase.entity.StaffBaseSalary;
import com.trtm.iot.staffbase.entity.StaffBaseWork;
import com.trtm.iot.staffbase.mapper.StaffBaseWorkMapper;
import com.trtm.iot.staffbase.service.IStaffBaseWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: staff_base_work
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
@Service
public class StaffBaseWorkServiceImpl extends ServiceImpl<StaffBaseWorkMapper, StaffBaseWork> implements IStaffBaseWorkService {

    @Autowired
    private StaffBaseWorkMapper staffBaseWorkMapper;
    @Override
    public List<Map> getStatistics(String orgcode,String start) {
        return staffBaseWorkMapper.getStatistics(orgcode, start);
    }

    @Override
    public List<StaffBaseSalary> getMothonHours(String orgcode, String start ,Integer pageNo,Integer pageSize) {
        return staffBaseWorkMapper.getMothonHours(orgcode, start,pageNo,pageSize);
    }
}
