package com.trtm.iot.rebarWzcailiaonamedictMan.mapper;

import java.util.List;

import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictManVo;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictMan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: rebar_wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2024-12-10
 * @Version: V1.0
 */
public interface RebarWzcailiaonamedictManMapper extends BaseMapper<RebarWzcailiaonamedictMan> {

    List<RebarWzcailiaonamedictManVo> getRebarWzcailiaoList(@Param("startDate")  String startDate,
                                                            @Param("endDate")  String  endDate);

    String getUsedNumber(@Param("caiLiaoNo") String caiLiaoNo,@Param("sysOrgCode")  String sysOrgCode,@Param("sysOrgCodes")  String sysOrgCodes);
}
