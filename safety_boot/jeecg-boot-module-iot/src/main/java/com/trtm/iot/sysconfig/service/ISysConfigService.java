package com.trtm.iot.sysconfig.service;

import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 定时任务配置表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
public interface ISysConfigService extends IService<SysConfig> {


    public List<BhzCementBase> selectListbai(Integer id);

    public List<BhzCementDetail> selectListbhzdetail(String batchNo);

    SysConfig selectsysconfigone(Integer cfgtype);

    /**
     * 根据传过来的唯一值来修改当前判断到的数据id
     * @param cfgtype
     * @param curid
     * @return
     */
    boolean updateSysConfig(Integer cfgtype, Integer curid);
    boolean updateSysConfigDate(Integer cfgtype, String curdate);

    boolean updateSysConfigs(Integer cfgtype, Integer curid,Integer Isworking);

    boolean updateSysConfigToken(Integer cfgtype,String token);
}
