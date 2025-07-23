package com.trtm.sy.syjcxm.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.sy.syjcxm.entity.SyDpsYyJcxm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: sy_dps_yy_jcxm
 * @Author: jeecg-boot
 * @Date:   2023-06-28
 * @Version: V1.0
 */
public interface SyDpsYyJcxmMapper extends BaseMapper<SyDpsYyJcxm> {

    List<Map<String, Object>> getJcxmByDepartId(String orgCode);
}
