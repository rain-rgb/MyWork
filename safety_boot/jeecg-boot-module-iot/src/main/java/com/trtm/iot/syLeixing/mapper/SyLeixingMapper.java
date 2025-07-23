package com.trtm.iot.syLeixing.mapper;

import java.util.List;

import com.trtm.iot.syLeixing.entity.SyLeixing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 试验类型
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
public interface SyLeixingMapper extends BaseMapper<SyLeixing> {
    List<String> getAllByCailiaono();
}
