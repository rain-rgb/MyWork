package com.trtm.iot.hc_project.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_project.entity.HcProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 华测获取项目
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface HcProjectMapper extends BaseMapper<HcProject> {

    List<String> fingbhzSheBeiNo(String orgCode);

    @Select("select * from hc_token where projectid = #{projectid}")
    public Map selectByProjectid(@Param("projectid") String projectid);

}
