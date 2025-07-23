package com.trtm.iot.largesbconfigure.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: largesb_configure
 * @Author: jeecg-boot
 * @Date:   2023-09-12
 * @Version: V1.0
 */
public interface LargesbConfigureMapper extends BaseMapper<LargesbConfigure> {

    @Insert("INSERT INTO largesb_door_sbj (sys_org_code,shebei_name,shebei_no,sbj_name,sbj_no) VALUES (#{sysOrgCode},#{sbname},#{shebeiNo},#{sbname1},#{doorSbjno})")
    void saveDoorSbj(String sysOrgCode, String sbname, String shebeiNo, String sbname1, String doorSbjno);
}
