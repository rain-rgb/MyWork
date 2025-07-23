package com.trtm.iot.cunliangprocedureconfig.service;

import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 梁场台座管理表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface ICunliangProcedureConfigService extends IService<CunliangProcedureConfig> {

    int updatedata(String shebeino, String liangzuoname);

    int updatedatas(String shebeino, String liangzuoname);

    int selectSum();

    List<CunliangProcedureConfig> selectprocedureList();

    List<CunliangProcedureConfig> selectprocedureLists(String uuid);

    void updateStatus(String shebeino, String taizuoname);

    void updateStatus1(String shebeino, String taizuoname);
}
