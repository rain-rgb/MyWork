package com.trtm.iot.zhydhistory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 智慧用电历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
public interface DeviceElectricHistorydataMapper extends BaseMapper<DeviceElectricHistorydata> {
    @Update("update device_electric_historydata set alertstate = #{alertstate} where id = #{id}")
    int updatealertstate(Integer id, Integer alertstate);

    @Update("update device_electric_historydata set status = #{status},alertstate=#{alertstate} where id = #{id}")
    int updateStatus(Integer id, Integer status,Integer alertstate);

    List<DeviceElectricHistorydata> selectzhydhistoryone(Integer id, Integer alertstate);

    List<DeviceElectricHistorydata> selectHisList(Integer curid, String shebeilist);

    List<DeviceElectricHistorydata> selectListToSHYJ(String shebeiNo, Integer id);
}
