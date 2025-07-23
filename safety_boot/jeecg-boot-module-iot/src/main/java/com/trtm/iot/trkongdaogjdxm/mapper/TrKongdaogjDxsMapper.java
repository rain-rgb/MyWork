package com.trtm.iot.trkongdaogjdxm.mapper;

import java.util.List;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 孔道灌浆（定性检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface TrKongdaogjDxsMapper extends BaseMapper<TrKongdaogjDxs> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<TrKongdaogjDxs> selectByMainId(@Param("mainId") String mainId);
}
