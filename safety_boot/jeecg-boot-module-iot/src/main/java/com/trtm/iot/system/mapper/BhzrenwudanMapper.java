package com.trtm.iot.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.trtm.iot.system.entity.StatisticsAndPageVo;
import com.trtm.iot.system.entity.TaskVo;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务单浇筑令
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "1")
public interface BhzrenwudanMapper extends BaseMapper<Bhzrenwudan> {

    List<Bhzrenwudan> queryselectlist(String code, Integer station, String sysOrgCode);
    List<Bhzrenwudan> queryselectlist1(String code, Integer station, String sysOrgCode);

    List<Bhzrenwudan> selectLists(Integer curid);

    List<Map> getToDayrwdInfo(String orgCode, String shebeino );

    StatisticsAndPageVo getTaskStaBySysOrgCode(@Param("sysOrgCode") String sysOrgCode);

    List<TaskVo> getTaskToPM(@Param("code")String code);

    StatisticsAndPageVo getTaskSta2(@Param("orgCode")String orgCode,@Param("conspos") String conspos, @Param("dattim_begin")String dattim_begin,@Param("dattim_end") String dattim_end);
}
