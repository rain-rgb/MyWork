package com.trtm.iot.kongdaoyaj.mapper;

import java.util.List;
import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 孔道灌浆子表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
public interface KongdaoyajsMapper extends BaseMapper<Kongdaoyajs> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<Kongdaoyajs> selectByMainId(@Param("mainId") String mainId);
}
