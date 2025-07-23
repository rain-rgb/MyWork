package com.trtm.iot.ckqdatahandle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.entity.DevicePulpingHistoryData;
import com.trtm.iot.ckqdatahandle.vo.CementFlyAsh;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMixinStationDetailsMapper extends BaseMapper<DeviceMixinStationDetails> {

    List<String> selectMixinStationDetails(@Param("sid") String sid);

    List<DevicePulpingHistoryData> selectSixMixinStationDetails(@Param("nowDate") String nowDate);

    String countSixTheoryCement(@Param("nowDate") String nowDate);

    Double countSixRealityCement(@Param("nowDate") String nowDate);

    String countSixTheoryFlyAsh(@Param("nowDate") String nowDate);

    Double countSixRealityFlyAsh(@Param("nowDate") String nowDate);

    void updateDevicePulpingHistoryData(Integer id);

    void updateDevicePulpingHistoryDataTwo(Integer id);

    List<DevicePulpingHistoryData> selectSixMixinStation();

    Double countSixRealityCementTotle();

    Double countSixRealityFlyAshTotle();

    CementFlyAsh sumOneBidTwoMixinStationCFA();

    List<DeviceTrafficHistoryData> selectGarbageDateMixinStation();

    List<DeviceTrafficHistoryData> selectGarbageDateBigMixinStationD();

    Double selectMSDTotalCement(@Param("sid") String sid);

    Double selectMSDTotalFlyAsh(@Param("sid") String sid);


}
