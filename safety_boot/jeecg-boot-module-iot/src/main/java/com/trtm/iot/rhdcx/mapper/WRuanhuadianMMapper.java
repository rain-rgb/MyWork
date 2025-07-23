package com.trtm.iot.rhdcx.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.rhdcx.entity.WRuanhuadianM;

import java.util.List;

/**
 * @Description: w_ruanhuadian_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
public interface WRuanhuadianMMapper extends BaseMapper<WRuanhuadianM> {

    List<WRuanhuadianM> getListjt(String shebeilist, Integer curid);
}
