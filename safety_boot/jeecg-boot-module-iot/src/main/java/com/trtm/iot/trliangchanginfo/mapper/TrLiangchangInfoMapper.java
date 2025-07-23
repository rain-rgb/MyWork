package com.trtm.iot.trliangchanginfo.mapper;

import java.util.List;

import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.trliangchanginfo.entity.TrLiangchangInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: tr_liangchang_info
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface TrLiangchangInfoMapper extends BaseMapper<TrLiangchangInfo> {

    @Select("select * from sys_depart where org_code = #{orgCode}")
    SysDepart selectdepart(String orgCode);

    @Select("select * from sys_depart where id = #{id}")
    SysDepart selectbyfuid(String id);

    @Select("select * from sys_depart where id = #{id}")
    SysDepart selectbyfuid1(String id);
}
