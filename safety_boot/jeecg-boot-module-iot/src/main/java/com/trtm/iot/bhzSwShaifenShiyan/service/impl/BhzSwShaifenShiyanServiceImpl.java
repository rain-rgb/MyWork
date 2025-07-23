package com.trtm.iot.bhzSwShaifenShiyan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzSwShaifenShiyan.entity.BhzSwShaifenShiyan;
import com.trtm.iot.bhzSwShaifenShiyan.mapper.BhzSwShaifenShiyanMapper;
import com.trtm.iot.bhzSwShaifenShiyan.service.IBhzSwShaifenShiyanService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bhz_sw_shaifen_shiyan
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
@Service
public class BhzSwShaifenShiyanServiceImpl extends ServiceImpl<BhzSwShaifenShiyanMapper, BhzSwShaifenShiyan> implements IBhzSwShaifenShiyanService {

    @Override
    public BhzSwShaifenShiyan selectone(String shebeibianhao, Integer cailiaoid, String uuid) {
        try{
            QueryWrapper<BhzSwShaifenShiyan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sbjno",shebeibianhao);
            queryWrapper.eq("cailiaomingcheng",cailiaoid);
            queryWrapper.eq("jipeibiaozhun",uuid);
            queryWrapper.eq("isUse",1);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
