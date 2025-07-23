package com.trtm.iot.staffbase.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.trtm.iot.staffbase.entity.StaffBaseSalary;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.staffbase.entity.StaffBaseWork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: staff_base_work
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
public interface StaffBaseWorkMapper extends BaseMapper<StaffBaseWork> {
    @Select("SELECT count(1) total ,DATE_FORMAT( kaoqriq, '%Y-%m-%d' ) time FROM `staff_base_work` WHERE sys_org_code = #{orgcode}  and DATE_FORMAT( kaoqriq, '%Y-%m-%d' ) >= #{starttime}   GROUP BY DATE_FORMAT( kaoqriq, '%Y-%m-%d' ) ")
    List<Map> getStatistics( @Param("orgcode") String orgcode,@Param("starttime") String starttime);

    @Select("SELECT xingming AS username, shenfenz, sys_org_code, banzu, count( 1 ) AS paydays, " +
            "  FLOOR(SUM( CASE WHEN kaoqriq LIKE concat(#{starttime},'%')  THEN TIMESTAMPDIFF( SECOND, shangbantime, xiabantime ) ELSE 0 END )/ 3600) AS payhours  " +
            " FROM staff_base_work  WHERE sys_org_code LIKE concat(#{orgcode},'%')  AND kaoqriq LIKE concat(#{starttime},'%')  " +
            " GROUP BY  xingming, shenfenz ORDER BY paydays DESC  ")
    List<StaffBaseSalary> getMothonHours(@Param("orgcode") String orgcode, @Param("starttime") String starttime , @Param("pageNo")Integer pageNo, @Param("pageSize")Integer pageSize);
}
