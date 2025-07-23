package com.trtm.iot.hc_datalinkage.mapper;

import java.util.List;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 数据联动（摊铺数据）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
public interface HcDatalinkagePaveMapper extends BaseMapper<HcDatalinkagePave> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<HcDatalinkagePave> selectByMainId(@Param("mainId") String mainId);
}
