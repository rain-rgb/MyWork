package com.trtm.iot.deviceMixpileHistorydataOne.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.mapper.DeviceMixpileHistorydataOneMapper;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileHistorydataOnePage;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileOneHandler;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2021-10-24
 * @Version: V1.0
 */
@Service
public class DeviceMixpileHistorydataOneServiceImpl extends ServiceImpl<DeviceMixpileHistorydataOneMapper, DeviceMixpileHistorydataOne> implements IDeviceMixpileHistorydataOneService {
    @Resource
    DeviceMixpileHistorydataOneMapper deviceMixpileHistorydataOneMapper;
    @Override
    public List<DeviceMixpileHistorydataOne> selectjbzzone(Integer id, Integer alertstate) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneHourAgo = now.minusHours(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = oneHourAgo.format(formatter);
            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper=new QueryWrapper<>();
            queryWrapper.ge("id",id);
            queryWrapper.eq("alertstate", alertstate);
            // 获取1h前的数据进行超标判断
           // queryWrapper.le("pile_time",formattedTime);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceMixpileHistorydataOne> selectjbzzonelisj(Integer id, String alertstate) {
        try {
            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper=new QueryWrapper<>();
            queryWrapper.ge("id",id);
            queryWrapper.eq("shebeino",alertstate);
            queryWrapper.eq("alertstate", 0);
            queryWrapper.last("limit 1000");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Map<String, Object>> countList() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return deviceMixpileHistorydataOneMapper.countList(loginUser.getOrgCode()+"%");
    }

    @Override
    public List<DeviceMixpileOneHandler> selecthandlerecodes(DeviceMixpileOneHandler handlerrecodes) {
        return null;
    }

    @Override
    public List<DeviceMixpileHistorydataOne> selectjbzzones(Integer curid, int i) {
        return deviceMixpileHistorydataOneMapper.selectjbzzones(curid,i);
    }

    @Override
    public List<DeviceMixpileHistorydataOne> selectLists(String shebeilist, Integer curid) {
        return deviceMixpileHistorydataOneMapper.selectLists(shebeilist,curid);
    }

    @Override
    public DeviceMixpileHistorydataOne selectone(String shebeino, String pileno, String piletime) {
        return deviceMixpileHistorydataOneMapper.selectOnes(shebeino,pileno,piletime);
    }

    @Override
    public Integer findXiangMuZSsb(List<String> querySheBeiList, String time) {
        return deviceMixpileHistorydataOneMapper.findXiangMuZSsb(querySheBeiList,time);
    }

    @Override
    public Integer findYuJingSsb(List<String> querySheBeiList, String time) {
        return deviceMixpileHistorydataOneMapper.findYuJingSsb(querySheBeiList,time);
    }

    @Override
    public Integer findBiHeSsb(List<String> querySheBeiList, String time) {
        return deviceMixpileHistorydataOneMapper.findBiHeSsb(querySheBeiList,time);
    }

    @Override
    public Integer findXiangMuZSsbs(List<String> querySheBeiList, String time) {
        return deviceMixpileHistorydataOneMapper.findXiangMuZSsbs(querySheBeiList,time);
    }

    @Override
    public Integer findYuJingSsbs(List<String> querySheBeiList, String time) {
        return deviceMixpileHistorydataOneMapper.findYuJingSsbs(querySheBeiList,time);
    }

    @Override
    public Integer findBiHeSsbs(List<String> querySheBeiList, String time) {
        return deviceMixpileHistorydataOneMapper.findBiHeSsbs(querySheBeiList,time);
    }

    @Override
    public SysDepart getSysDepart(String sysOrgCode) {
        return deviceMixpileHistorydataOneMapper.getSysDepart(sysOrgCode);
    }

    @Override
    public DeviceMixpileHistorydataOne getFJ(DeviceMixpileHistorydataOne one, int i) {
        return deviceMixpileHistorydataOneMapper.getFJ(one.getShebeino(),one.getPileMileage(),one.getPileNo(),i);
    }

    @Override
    public List<DeviceMixpileHistorydataOnePage> selectJbList(String shebeino,String  pileMileage,String pileNo) {
        return deviceMixpileHistorydataOneMapper.selectJbList(shebeino, pileMileage,pileNo);
    }

    @Override
    public List<DeviceMixpileHistorydataOne> selebyshebeicuid(List<String> shebeiInfolist, Integer curid) {
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("shebeino",shebeiInfolist);
        queryWrapper.gt("id",curid);
        queryWrapper.last("limit 100");
        return deviceMixpileHistorydataOneMapper.selectList(queryWrapper);
    }

    @Override
    public List<DeviceMixpileHistorydataOne> selectListzlpz(String shebeiList, Integer curid) {
        return deviceMixpileHistorydataOneMapper.selectListzlpz(shebeiList, curid);
    }

    @Override
    public DeviceMixpileHistorydataOne selebysailno(String sbjno, String pileno) {
        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino",sbjno);
        queryWrapper.eq("pile_no",pileno);
        queryWrapper.last("limit 1");
        return deviceMixpileHistorydataOneMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean saveBatchs(List<DeviceMixpileHistorydataOne> list) {
        if (list.size() > 0){
            for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne :list){
                QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino",deviceMixpileHistorydataOne.getShebeino());
                queryWrapper.eq("pile_time",deviceMixpileHistorydataOne.getPileTime());
                queryWrapper.eq("pile_mileage",deviceMixpileHistorydataOne.getPileMileage());
                queryWrapper.eq("pile_no",deviceMixpileHistorydataOne.getPileNo());
                DeviceMixpileHistorydataOne deviceMixpileHistorydataOne1 = deviceMixpileHistorydataOneMapper.selectOne(queryWrapper);
                if (deviceMixpileHistorydataOne1 != null){
                    deviceMixpileHistorydataOne.setId(deviceMixpileHistorydataOne1.getId());
                    deviceMixpileHistorydataOneMapper.updateById(deviceMixpileHistorydataOne);
                }else {
                    deviceMixpileHistorydataOneMapper.insert(deviceMixpileHistorydataOne);
                }
            }
        }
        return true;
    }

    @Override
    public List<DeviceMixpileHistorydataOne> getCBFJ(DeviceMixpileHistorydataOne oneRecord, int i) {
        return deviceMixpileHistorydataOneMapper.getCBFJ(oneRecord.getShebeino(),oneRecord.getPileMileage(),oneRecord.getPileNo(),i);
    }

    @Override
    public List<DeviceMixpileHistorydataOne> selectListss(String shebeilist, Integer curid) {
        return deviceMixpileHistorydataOneMapper.selectListss(shebeilist,curid);
    }
}
