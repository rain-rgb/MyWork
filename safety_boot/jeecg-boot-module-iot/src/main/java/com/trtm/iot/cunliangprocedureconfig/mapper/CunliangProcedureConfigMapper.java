package com.trtm.iot.cunliangprocedureconfig.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 梁场台座管理表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface CunliangProcedureConfigMapper extends BaseMapper<CunliangProcedureConfig> {

    @Update("update cunliang_procedure_config set status = 0 where shebeino =#{shebeino} and liangzuoname =#{liangzuoname}")
    int updatedata(String shebeino, String liangzuoname);

    @Update("update cunliang_procedure_config set status1 = 0 where shebeino =#{shebeino} and liangzuoname =#{liangzuoname}")
    int updatedatas(String shebeino, String liangzuoname);

    @Select("select count(shebeino) from cunliang_procedure_config")
    int selectSum();

    List<CunliangProcedureConfig> selectprocedureList();

    List<CunliangProcedureConfig> selectprocedureLists(String uuid);

    @Update("update cunliang_procedure_config set status = 1 where shebeino =#{shebeino} and liangzuoname =#{taizuoname}")
    void updateStatus(String shebeino, String taizuoname);

    @Update("update cunliang_procedure_config set status1 = 1 where shebeino =#{shebeino} and liangzuoname =#{taizuoname}")
    void updateStatus1(String shebeino, String taizuoname);
}
