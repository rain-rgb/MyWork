package com.trtm.sy.registertable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.registertable.model.SyFormBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;

public interface SyFormBaseMapper extends BaseMapper<SyFormBase> {

    @Select("SELECT datatime FROM sy_record_sb WHERE sb_id = #{sbid}")
    List<String> datatimeExit(@Param("sbid") String sbid);

}
