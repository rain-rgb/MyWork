package com.trtm.iot.yj.mapper;

import com.trtm.iot.yj.entity.TrYajiang;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: tr_yajiang
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
public interface TrYajiangMapper extends BaseMapper<TrYajiang> {

    List<String> findSyJids(List<String> sheBs);
}
