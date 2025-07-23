package com.trtm.sy.sylxdps.mapper;


import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: sy_dps_jc_testitemtype
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface SyDpsJcTestitemtypeMapper extends BaseMapper<SyDpsJcTestitemtype> {

    List<SyDpsJcTestitemtype> queryItemByType(@Param("types") List<String> types, @Param("isAll") Boolean isAll, @Param("roleIds") List<String> roleIds);

    @Select("select role_id from sys_user_role where user_id=#{userId}")
    List<String> getRoleIds(@Param("userId") String userId);
}
