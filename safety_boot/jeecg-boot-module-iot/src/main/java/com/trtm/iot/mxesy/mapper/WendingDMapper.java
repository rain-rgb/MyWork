package com.trtm.iot.mxesy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.mxesy.entity.WendingD;

import java.util.List;

/**
 * @Description: w_wendingdu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-28
 * @Version: V1.0
 */
public interface WendingDMapper extends BaseMapper<WendingD> {

    List<WendingD> getListjt(String shebeilist, Integer curid);
}
