package com.trtm.iot.trkongdaogjdwm.mapper;

import java.util.List;
import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDws;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 孔道灌浆（定位检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface TrKongdaogjDwsMapper extends BaseMapper<TrKongdaogjDws> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<TrKongdaogjDws> selectByMainId(@Param("mainId") String mainId);
}
