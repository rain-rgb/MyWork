package com.trtm.iot.hctfysworkareapeiz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.hctfysworkareapeiz.entity.HcTfysworkareapeiz;

import java.util.List;

/**
 * @Description: 土方压实配置表
 * @Author: jeecg-boot
 * @Date:   2024-04-09
 * @Version: V1.0
 */
public interface HcTfysworkareapeizMapper extends BaseMapper<HcTfysworkareapeiz> {

    List<HcTfysworkareapeiz> tfys(String shebeilist, Integer curid);
}
