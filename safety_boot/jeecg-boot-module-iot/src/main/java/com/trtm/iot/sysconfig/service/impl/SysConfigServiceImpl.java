package com.trtm.iot.sysconfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.mapper.SysConfigMapper;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: 定时任务配置表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private BhzCementBaseMapper bhzCementBaseMapper;

    @Override
    public List<BhzCementBase> selectListbai(Integer id){

        return sysConfigMapper.selectListbai(id);
    }

    @Override
    public List<BhzCementDetail> selectListbhzdetail(String  batchNo) {

        return sysConfigMapper.selectListbhzdetail(batchNo);
    }

    /**
     * 根据当前传过来的定时任务类型来查找对应的数据
     * @param cfgtype
     * @return
     */
    @Override
    public SysConfig selectsysconfigone(Integer cfgtype) {
        return sysConfigMapper.selectsysconfigone(cfgtype);
    }

    /**
     * 根据传过来的唯一值来修改当前判断到的数据id
     * @param cfgtype
     * @param curid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysConfig(Integer cfgtype,Integer curid){
        try {
            SysConfig sysConfig=new SysConfig();
            sysConfig.setCfgtype(cfgtype);
            sysConfig.setCurid(curid);
            return this.update(sysConfig,new QueryWrapper<SysConfig>().lambda().eq(SysConfig ::getCfgtype,cfgtype));
        } catch (Exception e) {
            throw new JeecgBootException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysConfigDate(Integer cfgtype, String curdate) {
        try {
            SysConfig sysConfig=new SysConfig();
            sysConfig.setCfgtype(cfgtype);
            sysConfig.setCurdate(curdate);
            return this.update(sysConfig,new QueryWrapper<SysConfig>().lambda().eq(SysConfig ::getCfgtype,cfgtype));
        } catch (Exception e) {
            throw new JeecgBootException(e);
        }
    }

    @Override
    public boolean updateSysConfigs(Integer cfgtype, Integer curid,Integer Isworking) {
        try {
            SysConfig sysConfig=new SysConfig();
            sysConfig.setCfgtype(cfgtype);
            sysConfig.setCurid(curid);
            sysConfig.setIsworking(Isworking);
            return this.update(sysConfig,new QueryWrapper<SysConfig>().lambda().eq(SysConfig ::getCfgtype,cfgtype));
        } catch (Exception e) {
            throw new JeecgBootException(e);
        }
    }

    @Override
    public boolean updateSysConfigToken(Integer cfgtype, String token) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SysConfig sysConfig=new SysConfig();
            sysConfig.setCfgtype(cfgtype);
            sysConfig.setToken(token);
            sysConfig.setCurdate(sdf.format(new Date()));
            return this.update(sysConfig,new QueryWrapper<SysConfig>().lambda().eq(SysConfig ::getCfgtype,cfgtype));
        } catch (Exception e) {
            throw new JeecgBootException(e);
        }
    }

}
