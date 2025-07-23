package com.trtm.iot.staffbase.mapper;

import java.util.List;

import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: staff_base_info
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
public interface StaffBaseInfoMapper extends BaseMapper<StaffBaseInfo> {

    @Select("SELECT * FROM staff_base_info where renyuantype = #{renyuantype} and pushstatus = 0 and sys_org_code = #{orgcode} ")
    List<StaffBaseInfo> getPushList(@Param("renyuantype") String renyuantype,@Param("orgcode") String orgcode);

    @Select("SELECT userid FROM staff_base_info where userid is not null and sys_org_code = #{orgcode} ")
    List<String> userIdList(@Param("orgcode") String orgcode);
}
