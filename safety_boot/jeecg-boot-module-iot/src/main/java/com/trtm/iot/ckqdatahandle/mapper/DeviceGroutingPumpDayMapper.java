package com.trtm.iot.ckqdatahandle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceGroutingPumpDayMapper extends BaseMapper<DeviceGroutingPumpDay> {

    Double selectDeviceGroutingPumpDayTotal(@Param("sid") String sid);

    Double selectDGPDTodayTotalByBidCode(@Param("sid") String sid,@Param("time") String time);

    Double selectDGPDAllDayTotalByBidCode(@Param("sid") String sid,@Param("time") String time);

    DeviceTrafficHistoryData selectDTHYesterdayOneData(@Param("sid") String sid,@Param("pid") String pid,@Param("nowData") String nowData);

    List<String> selectDayTotalByTime();

    List<DeviceGroutingPumpDay> selectDeviceGroutingPumpDays(@Param("bidCode") String bidCode,@Param("groutingPumpName") String groutingPumpName,@Param("time") String time);

     Double selectDGPDTodayTotal(@Param("bidCode") String bidCode,@Param("groutingPumpName") String groutingPumpName,@Param("time") String time);

     Double selectDGPDAllDayTotal(@Param("bidCode") String bidCode,@Param("groutingPumpName") String groutingPumpName,@Param("time") String time);
}
