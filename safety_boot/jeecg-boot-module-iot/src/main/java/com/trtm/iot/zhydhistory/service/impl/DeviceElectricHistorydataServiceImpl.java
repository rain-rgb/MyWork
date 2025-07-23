package com.trtm.iot.zhydhistory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.mapper.DeviceElectricHistorydataMapper;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 智慧用电历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
@Service
public class DeviceElectricHistorydataServiceImpl extends ServiceImpl<DeviceElectricHistorydataMapper, DeviceElectricHistorydata> implements IDeviceElectricHistorydataService {

    @Autowired
    private DeviceElectricHistorydataMapper deviceElectricHistorydataMapper;

    @Override
    public int updatezhydone(Integer id, Integer alertstate) {

        return deviceElectricHistorydataMapper.updatealertstate(id,alertstate);
    }

    @Override
    public void updateStatus(Integer id, Integer status,Integer alertstate) {
        deviceElectricHistorydataMapper.updateStatus(id,status,alertstate);
    }

    @Override
    public DeviceElectricHistorydata selectzhydhistorysta(Integer id) {
        try {
            QueryWrapper<DeviceElectricHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            queryWrapper.eq("status",2);
            queryWrapper.eq("alertstate",1);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceElectricHistorydata> selectLists(List<String> strsToList1, Integer curid) {
        try{
            QueryWrapper<DeviceElectricHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id",curid);
            queryWrapper.in("imei",strsToList1);
            queryWrapper.eq("alertstate",1);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceElectricHistorydata> selectHisList(Integer curid, String shebeilist) {

        return  deviceElectricHistorydataMapper.selectHisList(curid,shebeilist);
    }


    @Override
    public List<DeviceElectricHistorydata> selectListToSHYJ(String shebeiNo, Integer id) {
        return  deviceElectricHistorydataMapper.selectListToSHYJ(shebeiNo,id);
    }

    /**
     * 根据条件查出对应的数据
     * @param id
     * @param alertstate
     * @return
     */
    @Override
    public List<DeviceElectricHistorydata> selectzhydhistoryone(Integer id, Integer alertstate) {
//        try{
//            QueryWrapper<DeviceElectricHistorydata> queryWrapper = new QueryWrapper<>();
//            queryWrapper.ge("id",id);
//            queryWrapper.eq("alertstate",alertstate);
//            queryWrapper.last("limit 100");
//            return this.list(queryWrapper);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
        return deviceElectricHistorydataMapper.selectzhydhistoryone(id,alertstate);
    }
    /**
     * 根据设备编号查询智慧用电表数据
     * @param imei
     * @return
     */
    @Override
    public List<DeviceElectricHistorydata> selectzhydhistorybaselist(String imei,Integer id) {
        try{
            QueryWrapper<DeviceElectricHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            queryWrapper.eq("imei",imei);
            return this.list(queryWrapper);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
