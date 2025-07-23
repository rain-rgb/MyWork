package com.trtm.iot.hc_truck.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 获取运输车信息
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface HcTruckMapper extends BaseMapper<HcTruck> {

    List<String> fingbhzSheBeiNo(String orgCode);

    @Select("select truckId from hc_truck where orgcode like concat(#{sys_org_code},'%') and truckType = #{sbType}")
    List<String> selectbyorgcode(String sys_org_code, Integer sbType);
}
