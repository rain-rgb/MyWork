package com.trtm.iot.ckqdatahandle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceGroutingPumpMapper extends BaseMapper<DeviceGroutingPump> {

    List<String> selectDeviceGroutingPumpBySid(@Param("sid") String sid);

    Integer selectGroutingWellsNumber(@Param("bidCode") String bidCode, @Param("groutingPumpName") String groutingPumpName);

    Double selectGroutingTotal(@Param("bidCode") String bidCode, @Param("groutingPumpName") String groutingPumpName);

    /*
     * @Author zgq
     * @Description 查询流量计垃圾数据
     * @Date 2021/7/27 15:03
     * @Param
     * @return
     **/
    List<DeviceTrafficHistoryData> handleTrafficHistoryDataGarbageFM();

    /*
     * @Author zgq
     * @Description 查询压力和井号垃圾数据
     * @Date 2021/7/27 15:03
     * @Param
     * @return
     **/
    List<DeviceTrafficHistoryData>  handleTrafficHistoryDataGarbagePG();

    /*
     * @Author zgq
     * @Description 处理六标垃圾数据(六标流量计数据单独传的，topic='6_1')
     * @Date 2021/8/2 10:32
     * @Param
     * @return
     **/

    List<DeviceTrafficHistoryData> handleSixTrafficHistoryDataGarbageFM();

    List<DeviceTrafficHistoryData> selectHistoryDataByPid(@Param("pid") String pid);
}
