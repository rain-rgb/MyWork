package com.trtm.iot.jtwbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: jtwbs
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface JtwbsMapper extends BaseMapper<Jtwbs> {

    @Select("select * from jtwbs_copy1 where sys_org_code = #{orgCode}")
    Jtwbs selectbycogcode(String orgCode);

    Jtwbs getJtwbs(String sysOrgCode);
}
