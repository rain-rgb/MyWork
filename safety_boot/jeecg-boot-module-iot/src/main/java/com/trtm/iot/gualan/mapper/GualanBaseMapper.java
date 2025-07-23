package com.trtm.iot.gualan.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.gualan.entity.GualanBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 挂篮数据主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
public interface GualanBaseMapper extends BaseMapper<GualanBase> {

    List<Map<String, Object>> getYjList();

    List<GualanBase> selectGuaLanList(String shebeilist, Integer curid);

    Map<String, Object> getMap2Port();
}
