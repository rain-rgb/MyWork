package com.trtm.iot.renwudan.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 拌合站任务单进度
 * @Author: jeecg-boot
 * @Date:   2021-06-16
 * @Version: V1.0
 */
public interface RenwudanScheduleMapper extends BaseMapper<RenwudanSchedule> {

    @Update("update renwudan_schedule set metes=#{metes},bfb=#{bfb},endtim=#{endtime} ,dish_count=#{dishCount} where id=#{id}")
    int updatealertsate(double metes, double bfb, Integer id, Date endtime, Integer dishCount);

    RenwudanSchedule queryones(String code, String shebei_no);

    List<RenwudanSchedule> selectrwdschedule(String code, String shebeiNo);

    @Select("select Metes,shebei_no,sys_org_code,BetLev,Mete,Projectname,ConsPos from renwudan_schedule where Code = #{code} and station = #{station}  and isdel = 0 and sys_org_code like #{sys_depart_orgcode}")
    Map<String,Object> selectmetes(String code, Integer station,String sys_depart_orgcode);

    @Select("select Metes,shebei_no,sys_org_code,BetLev,Mete,Projectname,ConsPos from renwudan_schedule where Code = #{code}and isdel = 0 and sys_org_code like #{orgcode}")
    List<RenwudanSchedule> selectmetesd(String code, String orgcode);

    @Update("update renwudan_schedule set metes=#{metes},bfb=#{bfb},endtim=#{endtime} ,dish_count=#{dishCount},ConsPos=#{conspos} where id=#{id}")
    int updateonezt(double metes, double bfb, Integer id, Date endtime, Integer dishCount, String conspos);
}
