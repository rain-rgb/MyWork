package com.trtm.iot.ckqdatahandle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMixinStationMapper extends BaseMapper<DeviceMixinStation> {

    String selectMixinStation(@Param("sid") String sid);

    /*
     * @Author zgq
     * @Description 一标二号站水泥
     * @Date 2021/7/26 17:43
     * @Param
     * @return
     **/
    List<DeviceTrafficHistoryData> selectDTHOneBidTwoMixinStationCT1STotal();

    /*
     * @Author zgq
     * @Description 一标二号站粉煤灰
     * @Date 2021/7/26 17:44
     * @Param
     * @return
     **/
    List<DeviceTrafficHistoryData> selectDTHOneBidTwoMixinStationFLY1STotal();
}
