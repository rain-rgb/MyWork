package com.trtm.iot.clgl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 车辆信息实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-13
 * @Version: V1.0
 */
public interface ClxxRealdataMapper extends BaseMapper<ClxxRealdata> {

    ClxxRealdata queryone(String shebeino);

    @Select("select depart_name from sys_depart where org_code = #{sysOrgCode}")
    String getDepartName(String sysOrgCode);

    @Update("update front_device_realdata set status = 0 where id = #{id}")
    void updateStatusById(Integer id);
}
