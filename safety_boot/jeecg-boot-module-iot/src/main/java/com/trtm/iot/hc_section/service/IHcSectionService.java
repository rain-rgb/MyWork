package com.trtm.iot.hc_section.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_section.entity.HcSection;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 华测获取标段
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface IHcSectionService extends IService<HcSection> {


    String listByOrgCode(String code);
}
