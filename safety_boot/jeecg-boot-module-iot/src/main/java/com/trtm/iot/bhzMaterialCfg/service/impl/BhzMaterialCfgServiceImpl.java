package com.trtm.iot.bhzMaterialCfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzMaterialCfg.entity.BhzMaterialCfg;
import com.trtm.iot.bhzMaterialCfg.mapper.BhzMaterialCfgMapper;
import com.trtm.iot.bhzMaterialCfg.service.IBhzMaterialCfgService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bhz_material_cfg
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzMaterialCfgServiceImpl extends ServiceImpl<BhzMaterialCfgMapper, BhzMaterialCfg> implements IBhzMaterialCfgService {

    @Override
    public BhzMaterialCfg selectone(String bhjno, String specs) {
        try {
            QueryWrapper<BhzMaterialCfg> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("bhjno",bhjno);
            queryWrapper.eq("specs",specs);
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
