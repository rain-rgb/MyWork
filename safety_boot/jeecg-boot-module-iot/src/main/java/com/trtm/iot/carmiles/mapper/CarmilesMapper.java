package com.trtm.iot.carmiles.mapper;

import java.util.List;

import com.trtm.iot.system.entity.ShebeiInfo;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 车辆里程
 * @Author: jeecg-boot
 * @Date:   2022-03-08
 * @Version: V1.0
 */
public interface CarmilesMapper extends BaseMapper<Carmiles> {

    @Select("select * from car_miles where shebei_no = #{shebeino} and rundate = #{day}")
    List<Carmiles> getCarmiles(String shebeino, String day);

}
