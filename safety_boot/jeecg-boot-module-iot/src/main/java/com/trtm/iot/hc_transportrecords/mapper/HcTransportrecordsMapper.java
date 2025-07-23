package com.trtm.iot.hc_transportrecords.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 运输数据
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
public interface HcTransportrecordsMapper extends BaseMapper<HcTransportrecords> {

    Integer selectsum(List<String> querySheBeiList, String dateNowStr);
}
