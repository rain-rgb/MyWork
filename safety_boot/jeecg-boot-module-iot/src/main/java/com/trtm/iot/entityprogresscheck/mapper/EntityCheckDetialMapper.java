package com.trtm.iot.entityprogresscheck.mapper;

import java.util.List;
import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 实体进度清单子表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
public interface EntityCheckDetialMapper extends BaseMapper<EntityCheckDetial> {


	public boolean deleteByMainId(@Param("mainId") String mainId);

	public List<EntityCheckDetial> selectByMainId(@Param("mainId") String mainId);
}
