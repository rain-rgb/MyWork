package com.trtm.iot.cunliangprocedureconfig.service.impl;

import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.mapper.CunliangProcedureConfigMapper;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 梁场台座管理表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Service
public class CunliangProcedureConfigServiceImpl extends ServiceImpl<CunliangProcedureConfigMapper, CunliangProcedureConfig> implements ICunliangProcedureConfigService {

    @Autowired CunliangProcedureConfigMapper cunliangProcedureConfigMapper;

    @Override
    public int updatedata(String shebeino, String liangzuoname) {
        return cunliangProcedureConfigMapper.updatedata(shebeino,liangzuoname);
    }

    @Override
    public int updatedatas(String shebeino, String liangzuoname) {
        return cunliangProcedureConfigMapper.updatedatas(shebeino,liangzuoname);
    }

    @Override
    public int selectSum() {
        return cunliangProcedureConfigMapper.selectSum();
    }

    @Override
    public List<CunliangProcedureConfig> selectprocedureList() {
        return cunliangProcedureConfigMapper.selectprocedureList();
    }

    @Override
    public List<CunliangProcedureConfig> selectprocedureLists(String uuid) {
        return cunliangProcedureConfigMapper.selectprocedureLists(uuid);
    }

    @Override
    public void updateStatus(String shebeino, String taizuoname) {
        cunliangProcedureConfigMapper.updateStatus(shebeino,taizuoname);
    }

    @Override
    public void updateStatus1(String shebeino, String taizuoname) {
        cunliangProcedureConfigMapper.updateStatus1(shebeino,taizuoname);
    }
}
