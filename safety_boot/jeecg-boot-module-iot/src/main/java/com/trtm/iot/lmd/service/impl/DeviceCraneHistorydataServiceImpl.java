package com.trtm.iot.lmd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.mapper.DeviceCraneHistorydataMapper;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 龙门吊表信息
 * @Author: jeecg-boot
 * @Date: 2021-07-28
 * @Version: V1.0
 */
@Service
public class DeviceCraneHistorydataServiceImpl extends ServiceImpl<DeviceCraneHistorydataMapper, DeviceCraneHistorydata> implements IDeviceCraneHistorydataService {

    @Autowired
    DeviceCraneHistorydataMapper deviceCraneHistorydataMapper;

    @Override
    public List<DeviceCraneHistorydata> selectLists(List<String> shebeiNo, Integer id) {
        try {
            QueryWrapper<DeviceCraneHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.in("device_code", shebeiNo);
            queryWrapper.groupBy("cranedate");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceCraneHistorydata> selectListsHistoryList(List<String> shebeiNo, Integer id, int alertstate) {
        try {
            QueryWrapper<DeviceCraneHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.in("device_code", shebeiNo);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.last("limit 200");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceCraneHistorydata> selectcranehistoryone(Integer curid, int alertstate) {
        try {
            QueryWrapper<DeviceCraneHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", curid);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateStatus(int id, int status) {
        return deviceCraneHistorydataMapper.updateStatus(id, status);
    }

    @Override
    public int updatebysone(int id, Integer alertstate) {
        return deviceCraneHistorydataMapper.updatebysone(id, alertstate);
    }

    @Override
    public List<DeviceCraneHistorydata> selectListsHistoryList2(String split, Integer curid, int i) {
        return deviceCraneHistorydataMapper.selectListsHistoryList2(split, curid, i);

    }

    @Override
    public List<DeviceCraneHistorydata> selectListToSHYJ(String shebeiNo, Integer id) {
        return deviceCraneHistorydataMapper.selectListToSHYJ(shebeiNo, id);

    }

    @Override
    public List<DeviceCraneHistorydata> selectListbim(String shebeiNo, Integer id) {
        return deviceCraneHistorydataMapper.selectListbim(shebeiNo, id);

    }

    @Override
    public List<ShebeiInfo> getShebei(int sbtype, String orgCode) {
        return deviceCraneHistorydataMapper.getShebei(sbtype, orgCode);
    }

    @Override
    public List<DeviceCraneHistorydata> selectLmdList(String shebeilist, Integer curid) {
        return deviceCraneHistorydataMapper.selectLmdList(shebeilist, curid);
    }

    @Override
    public List<Map<String, Object>> getYjList() {
        return deviceCraneHistorydataMapper.getYjList();
    }

    @Override
    public List<DeviceCraneHistorydata> selectListtoJG(String shebeilist, Integer curid) {
        return deviceCraneHistorydataMapper.selectListtoJG(shebeilist, curid);
    }
}
