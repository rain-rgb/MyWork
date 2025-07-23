package com.trtm.iot.devicecranerealdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 龙门吊实时数据
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface DeviceCraneRealdataMapper extends BaseMapper<DeviceCraneRealdata> {

    DeviceCraneRealdata queryone(String shebeiNo);

    List<DeviceCraneRealdata> selectListBySB(List<String> shebeiList);
}
