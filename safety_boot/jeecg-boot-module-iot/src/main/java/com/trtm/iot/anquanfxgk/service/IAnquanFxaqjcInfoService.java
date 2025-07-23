package com.trtm.iot.anquanfxgk.service;

import com.trtm.iot.anquanfxgk.entity.AnquanFxaqjcInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: anquan_fxaqjc_info
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
public interface IAnquanFxaqjcInfoService extends IService<AnquanFxaqjcInfo> {
    /**根据当前日期，判断是否整改超时调整状态
     * @return
     */
    Integer modifyFxaqjcTypeByZhenggaiTime();
}
