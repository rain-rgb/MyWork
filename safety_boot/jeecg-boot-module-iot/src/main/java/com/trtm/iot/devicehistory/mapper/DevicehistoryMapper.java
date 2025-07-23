package com.trtm.iot.devicehistory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 环境监测历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
public interface DevicehistoryMapper extends BaseMapper<Devicehistory> {


    List<Devicehistory> selectListes(String DevAddr, Integer id);

    List<Devicehistory> selectdeviceone(Integer curid, int alertstate);

    Devicehistory queryone(String shebeiNo);

    List<Devicehistory> selectlistdata(String strsToList1, Integer curid);

    List<Devicehistory> selectlistToSHYJ(String shebeiNo, Integer id);

    List<Devicehistory> selectlistYJQS(String shebeiNo, Integer id);
    List<Devicehistory> selectlistYLQ(String shebeiNo, Integer id);

    List<Devicehistory> selectlistdatatop1(String strsToList1, Integer curid);

    List<Devicehistory> selectListByDev(String devAddr);

    Devicehistory selectNewByDev(String shebeino);

    List<Devicehistory> selectyjList(String shebeilist, Integer curid);
}
