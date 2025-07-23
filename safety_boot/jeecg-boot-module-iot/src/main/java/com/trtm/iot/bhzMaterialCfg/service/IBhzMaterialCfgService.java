package com.trtm.iot.bhzMaterialCfg.service;

import com.trtm.iot.bhzMaterialCfg.entity.BhzMaterialCfg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bhz_material_cfg
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzMaterialCfgService extends IService<BhzMaterialCfg> {

    BhzMaterialCfg selectone(String bhjno,String specs);
}
