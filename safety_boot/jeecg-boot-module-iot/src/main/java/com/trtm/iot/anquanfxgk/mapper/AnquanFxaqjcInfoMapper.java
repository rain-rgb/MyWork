package com.trtm.iot.anquanfxgk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.anquanfxgk.entity.AnquanFxaqjcInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: anquan_fxaqjc_info
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
public interface AnquanFxaqjcInfoMapper extends BaseMapper<AnquanFxaqjcInfo> {
    /**根据当前日期，判断是否整改超时调整状态
     * @return
     */
    Integer modifyFxaqjcTypeByZhenggaiTime();
}
