package com.trtm.iot.bhzcementworkstatus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcementworkstatus.entity.BhzCementWorkstatus;
import com.trtm.iot.bhzcementworkstatus.mapper.BhzCementWorkstatusMapper;
import com.trtm.iot.bhzcementworkstatus.service.IBhzCementWorkstatusService;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 砼拌合站工作状态表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
@Service
public class BhzCementWorkstatusServiceImpl extends ServiceImpl<BhzCementWorkstatusMapper, BhzCementWorkstatus> implements IBhzCementWorkstatusService {

    @Autowired
    private BhzCementWorkstatusMapper bhzCementWorkstatusMapper;
    @Override
    public BhzCementWorkstatus queryone(String shebeiNo, String sysOrgCode) {
//        try{
//            QueryWrapper<BhzCementWorkstatus> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("shebei_no",shebeiNo);
//            queryWrapper.eq("sys_org_code",sysOrgCode);
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return bhzCementWorkstatusMapper.queryone(shebeiNo,sysOrgCode);
    }

    @Override
    public int updatestatus(int id, int status) {
        return bhzCementWorkstatusMapper.updatestatus(id,status);
    }
}
