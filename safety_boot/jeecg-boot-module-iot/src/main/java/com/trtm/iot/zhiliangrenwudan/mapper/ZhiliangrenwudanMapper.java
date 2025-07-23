package com.trtm.iot.zhiliangrenwudan.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 任务单（制梁）表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-13
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "1")
public interface ZhiliangrenwudanMapper extends BaseMapper<Zhiliangrenwudan> {

    @Select("select count(BetLev) from zhilaingrenwudan where BetLev = #{betlev}")
    int selectBetlevSum(String betlev);

    List<Zhiliangrenwudan> getzhilianglist();

    @Select("SELECT uuid FROM zhiliangrenwudan WHERE sys_org_code = #{sys_depart_orgcode} ORDER BY id DESC LIMIT 1 OFFSET 0")
    Zhiliangrenwudan selectbyorgCode(String sys_depart_orgcode);
}
