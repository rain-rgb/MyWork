package com.trtm.iot.rebarPersonnel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.rebarPersonnel.entity.RebarPersonnel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: rebar_personnel
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
public interface RebarPersonnelMapper extends BaseMapper<RebarPersonnel> {

    List<RebarPersonnel> getLeadTeamByFactory(@Param("factoryId") String factoryId);
}
