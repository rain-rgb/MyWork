package com.trtm.iot.zhydreal.mapper;

import java.util.List;

import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 智慧用电实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
public interface DeviceElectricRealdataMapper extends BaseMapper<DeviceElectricRealdata> {

    @Update("update device_electric_realdata set alertstate = #{alertstate} where id = #{id}")
    int updatealertstate(Integer id,Integer alertstate);

    @Update("update device_electric_realdata set status = #{status} where id = #{id}")
    int updateStatus(Integer id, Integer status);

    List<DeviceElectricRealdata> selectzhydone(Integer id);

    List<DeviceElectricHistorydata> selectRealList(Integer curid, String shebeilist);
}
