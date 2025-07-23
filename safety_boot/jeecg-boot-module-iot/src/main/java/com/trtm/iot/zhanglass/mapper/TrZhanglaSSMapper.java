package com.trtm.iot.zhanglass.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 张拉过程子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
public interface TrZhanglaSSMapper extends BaseMapper<TrZhanglaSS> {

    List<TrZhanglaSS> getselectList(String holeid);
}
