package com.trtm.iot.lowstrain_m.mapper;

import java.util.List;
import com.trtm.iot.lowstrain_m.entity.LowstrainS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 低应变子表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
public interface LowstrainSMapper extends BaseMapper<LowstrainS> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<LowstrainS> selectByMainId(@Param("mainId") String mainId);
}
