package com.trtm.iot.hc_section.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_section.entity.HcSection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 华测获取标段
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface HcSectionMapper extends BaseMapper<HcSection> {

    @Select("select GROUP_CONCAT( sectionId SEPARATOR ',')  from hc_section where orgcode LIKE  CONCAT(#{code},\"%\") ")
    String listByOrgCode(String code);
}
