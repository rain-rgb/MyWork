package com.trtm.iot.hc_pavement_stationdata.mapper;

import java.util.List;

import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdataP;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 获取逐桩数据
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
public interface HcPavementStationdataMapper extends BaseMapper<HcPavementStationdata> {

    List<HcPavementStationdataP> getData(String carNumber);

    HcPavementStationdata getavg(String start, String end);

}
