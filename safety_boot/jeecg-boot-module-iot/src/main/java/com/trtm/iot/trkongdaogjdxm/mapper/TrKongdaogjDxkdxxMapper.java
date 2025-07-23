package com.trtm.iot.trkongdaogjdxm.mapper;

import java.util.List;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxkdxx;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 孔道灌浆（定性检测）孔道信息表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface TrKongdaogjDxkdxxMapper extends BaseMapper<TrKongdaogjDxkdxx> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<TrKongdaogjDxkdxx> selectByMainId(@Param("mainId") String mainId);
}
