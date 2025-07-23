package com.trtm.iot.trmaoxiayuyinglim.mapper;

import java.util.List;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 锚下预应力张拉子表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
public interface TrMaoxiayuyingliSMapper extends BaseMapper<TrMaoxiayuyingliS> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<TrMaoxiayuyingliS> selectByMainId(@Param("mainId") String mainId);
}
