package com.trtm.iot.bhzcfg.mapper;

import java.util.List;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 拌合站超标配置子表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
public interface BhzChaobiaoCfgMapper extends BaseMapper<BhzChaobiaoCfg> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	@Select("select * from bhz_chaobiao_cfg where uid = #{mainId}")
	public List<BhzChaobiaoCfg> selectByMainIds(@Param("mainId") String mainId);
}
