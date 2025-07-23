package com.trtm.iot.shebeiWarncfg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.shebeiWarncfg.entity.ShebeiWarncfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 设备状态预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-08-04
 * @Version: V1.0
 */
public interface ShebeiWarncfgMapper extends BaseMapper<ShebeiWarncfg> {

    ShebeiWarncfg getcfgdata(String sbjno);
}
