package com.trtm.iot.frontDeviceWeilan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.frontDeviceWeilan.entity.FrontDeviceWeilan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 运输车电子围栏数据
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
public interface FrontDeviceWeilanMapper extends BaseMapper<FrontDeviceWeilan> {

    List<FrontDeviceWeilan> querylist(Integer status,Integer isdel,Integer delstatus);
}
