package com.trtm.iot.rebarFactory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.rebarFactory.entity.RebarFactory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: rebar_factory
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
public interface RebarFactoryMapper extends BaseMapper<RebarFactory> {

    String getFactoryName(@Param("factoryId") String factoryId);
}
