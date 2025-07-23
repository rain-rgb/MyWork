package com.trtm.iot.wzyclchuchanggbman.service;

import com.trtm.iot.wzyclchuchanggbman.entity.WzyclchuchanggbMan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;

import java.util.List;

/**
 * @Description: wzyclchuchanggb_man
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
public interface IWzyclchuchanggbManService extends IService<WzyclchuchanggbMan> {

    Boolean saveListWzycljinchanggb(List<WzyclchuchanggbMan> list);
}
