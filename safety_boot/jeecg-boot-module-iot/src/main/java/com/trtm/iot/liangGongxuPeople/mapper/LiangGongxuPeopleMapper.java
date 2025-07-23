package com.trtm.iot.liangGongxuPeople.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.liangGongxuPeople.entity.LiangGongxuPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 自动工序负责人配置
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface LiangGongxuPeopleMapper extends BaseMapper<LiangGongxuPeople> {

    @Select("select * from liang_gongxu_people where sys_org_code = #{sysOrgCode} and xuhao = #{xuhao}")
    LiangGongxuPeople selectResponsible(String sysOrgCode, Integer xuhao);
}
