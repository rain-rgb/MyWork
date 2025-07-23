package com.trtm.iot.bhzSupervisionOrderSub.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzSupervisionOrderSub.entity.BhzSupervisionOrderSub;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bhz_supervision_order_sub
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface BhzSupervisionOrderSubMapper extends BaseMapper<BhzSupervisionOrderSub> {

    String selectSysAnnouncement(String bhzNo);
}
