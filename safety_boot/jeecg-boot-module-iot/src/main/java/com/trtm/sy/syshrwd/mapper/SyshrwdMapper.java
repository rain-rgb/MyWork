package com.trtm.sy.syshrwd.mapper;

import java.util.List;

import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 试验收货任务单
 * @Author: jeecg-boot
 * @Date:   2022-09-08
 * @Version: V1.0
 */
public interface SyshrwdMapper extends BaseMapper<Syshrwd> {

    List<Syshrwd> sendMsg( );
}
