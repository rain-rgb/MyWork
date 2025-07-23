package com.trtm.iot.ckqhistorydata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import com.trtm.iot.ckqhistorydata.vo.LookDeviceTrafficHistoryDataVo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Repository
public interface DeviceTrafficHistoryDataMapper extends BaseMapper<DeviceTrafficHistoryData> {

    List<DeviceTrafficHistoryData> selectWellsNumber(@Param("topic") String topic,@Param("pid") String pid);

    List<DeviceTrafficHistoryData> selectDeviceMixinStation(@Param("topic") String topic);

    List<DeviceTrafficHistoryData> selectGarbageDate();

    List<DeviceTrafficHistoryData> selectGarbageDateBigMixinStation();

    String  countTheoryCement(@Param("nowData") String nowData);

    String  countRealityCement(@Param("nowData") String nowData);

    String  countTheoryFlyAsh(@Param("nowData") String nowData);

    String  countRealityFlyAsh(@Param("nowData") String nowData);

    List<DeviceTrafficHistoryData> deleteTrafficHistoryDataGarbage();

    List<DeviceTrafficHistoryData> selectDTHBid(@Param("topic") String topic,@Param("nowData") String nowData,@Param("pid") String pid);

    Double  selectDTHBidCount(@Param("topic") String topic,@Param("nowData") String nowData,@Param("pid") String pid);

    List<String> selectDTHBidTheoryValues(@Param("topic") String topic,@Param("pid") String pid);

    List<DeviceTrafficHistoryData> selectDTHBidTheoryValuesSetStatus(@Param("topic") String topic,@Param("pid") String pid,@Param("v") String v,@Param("nowTime") Date nowTime);

    List<DeviceTrafficHistoryData> selectDTHOneBidTwoMixinStationFLY1S(@Param("nowTime") String nowTime);

    List<DeviceTrafficHistoryData> selectDTHOneBidTwoMixinStationCT1S(@Param("nowTime") String nowTime);

    List<DeviceTrafficHistoryData> selectDTHFourBidOneMixinStationGroupByV(@Param("pid") String pid,@Param("nowData") String nowData);

    Double selectDTHFourBidOneMixinStationSum(@Param("pid") String pid,@Param("nowData") String nowData,@Param("v") String v);

    List<DeviceTrafficHistoryData> selectDTHFourBidOneMixinStationList(@Param("pid") String pid,@Param("nowData") String nowData,@Param("v") String v);

    LookDeviceTrafficHistoryDataVo selectGroutingPumpRealTimeData(@Param("sid") String sid, @Param("pid") String pid);

    List<LookDeviceTrafficHistoryDataVo> selectGroutingPumpRealTotal(@Param("sid") String sid);

    List<DeviceTrafficHistoryData> selectSixFM(@Param("pid") String pid,@Param("sid") String sid);

    List<DeviceTrafficHistoryData> selectSixFMWhetherComplete(@Param("sid") String sid);
}
