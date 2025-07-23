package com.trtm.iot.tr_maoxiayuyingli_over_handler.service;

import com.trtm.iot.tr_maoxiayuyingli_over_handler.entity.TrMaoxiayuyingliOverHandler;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 锚下预应力张拉处置
 * @Author: jeecg-boot
 * @Date:   2024-06-06
 * @Version: V1.0
 */
public interface ITrMaoxiayuyingliOverHandlerService extends IService<TrMaoxiayuyingliOverHandler> {

    int chuZhiAddOrUpDate(String wtyy, String clfs, String cljg, String uuid, String bizPath, String chuzhiren);
}
