package com.trtm.iot.bhzSupervisionOrder.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bhz_supervision_order
 * @Author: jeecg-boot
 * @Date:   2023-02-08
 * @Version: V1.0
 */
public interface BhzSupervisionOrderMapper extends BaseMapper<BhzSupervisionOrder> {

    Integer selectCountByNo(String num);
}
