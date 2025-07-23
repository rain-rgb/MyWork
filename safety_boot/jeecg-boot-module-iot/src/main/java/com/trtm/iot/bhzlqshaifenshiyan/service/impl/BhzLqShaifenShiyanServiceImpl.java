package com.trtm.iot.bhzlqshaifenshiyan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzlqshaifenshiyan.entity.BhzLqShaifenShiyan;
import com.trtm.iot.bhzlqshaifenshiyan.mapper.BhzLqShaifenShiyanMapper;
import com.trtm.iot.bhzlqshaifenshiyan.service.IBhzLqShaifenShiyanService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 沥青筛分试验数据信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
@Service
public class BhzLqShaifenShiyanServiceImpl extends ServiceImpl<BhzLqShaifenShiyanMapper, BhzLqShaifenShiyan> implements IBhzLqShaifenShiyanService {

    @Override
    public BhzLqShaifenShiyan selectone(String shebeibianhao, Integer cailiaoid, String uuid) {
        try{
            QueryWrapper<BhzLqShaifenShiyan> queryWrapper = new QueryWrapper<>();
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
