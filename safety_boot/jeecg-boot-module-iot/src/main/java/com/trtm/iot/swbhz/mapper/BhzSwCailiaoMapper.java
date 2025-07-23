package com.trtm.iot.swbhz.mapper;

import java.util.List;

import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 水稳材料表信息
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface BhzSwCailiaoMapper extends BaseMapper<BhzSwCailiao> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<BhzSwCailiao> selectByMainId(@Param("mainId") String mainId);

    List<BhzCementDetail> selectdetail(String id);
}
