package com.trtm.sy.sylxqxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.sylxqxgl.entity.SyDpsYyUserAsTestItemType;
import com.trtm.sy.sylxqxgl.entity.SyJlbResponse;
import com.trtm.sy.sylxqxgl.entity.SyLxResponse;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SyDpsYyUserAsTestItemTypeMapper extends BaseMapper<SyDpsYyUserAsTestItemType> {

    @Select("SELECT * FROM sy_dps_jc_testitemtype WHERE titIsDel = 0 AND ( titParentCode IS NULL OR TRIM( titParentCode ) = '' ) ORDER BY titCode ASC")
    List<SyLxResponse> getParentSyLx();

    @Select("SELECT * FROM sy_dps_jc_testitemtype WHERE titIsDel = 0 AND titParentCode = #{titCode} ORDER BY titCode ASC")
    List<SyLxResponse> getSonsSyLx(String titCode);

    @Select("SELECT * FROM sy_dps_jc_testitem WHERE tiIsDel = 0 AND tiType != 1 AND titCode = #{sTitCode} ORDER BY tiSort ASC")
    List<SyJlbResponse> getSyJlbByTitCode(String sTitCode);

    @Select("SELECT titCode FROM sy_dps_yy_userastestitemtype WHERE role_id=#{roleId}")
    List<String> getAllocatedSy(String roleId);

    @Select("SELECT ti_nos FROM sy_dps_yy_userastestitemtype WHERE role_id=#{roleId} AND titCode=#{titCode}")
    List<String> getSyLxJlbByRoleId(String roleId, String titCode);
}
