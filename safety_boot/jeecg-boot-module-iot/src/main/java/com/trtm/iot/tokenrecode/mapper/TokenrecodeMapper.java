package com.trtm.iot.tokenrecode.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: tokenrecode
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
public interface TokenrecodeMapper extends BaseMapper<Tokenrecode> {

    @Select("select * from tokenrecode where sys_org_code like #{orgCode2}")
    List<Tokenrecode> seleycogcod(String orgCode2);

    @Select("select * from tokenrecode")
    List<Tokenrecode> cxqb();
}
