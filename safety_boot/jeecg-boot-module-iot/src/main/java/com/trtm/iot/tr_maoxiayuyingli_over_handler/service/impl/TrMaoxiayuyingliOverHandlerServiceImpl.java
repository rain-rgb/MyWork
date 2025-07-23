package com.trtm.iot.tr_maoxiayuyingli_over_handler.service.impl;

import com.trtm.iot.tr_maoxiayuyingli_over_handler.entity.TrMaoxiayuyingliOverHandler;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.mapper.TrMaoxiayuyingliOverHandlerMapper;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.service.ITrMaoxiayuyingliOverHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 锚下预应力张拉处置
 * @Author: jeecg-boot
 * @Date:   2024-06-06
 * @Version: V1.0
 */
@Service
public class TrMaoxiayuyingliOverHandlerServiceImpl extends ServiceImpl<TrMaoxiayuyingliOverHandlerMapper, TrMaoxiayuyingliOverHandler> implements ITrMaoxiayuyingliOverHandlerService {

    @Autowired
    private TrMaoxiayuyingliOverHandlerMapper trMaoxiayuyingliOverHandlerMapper;
    @Override
    public int chuZhiAddOrUpDate(String wtyy, String clfs, String cljg, String uuid, String bizPath, String chuzhiren) {

        String i = trMaoxiayuyingliOverHandlerMapper.getDataById(uuid);
        int result = 0;
        if (i==null){
            result = trMaoxiayuyingliOverHandlerMapper.chuZhiAddById(wtyy,clfs,cljg,uuid,bizPath,chuzhiren);
        }else {
            result = trMaoxiayuyingliOverHandlerMapper.chuZhiUpdateById(wtyy,clfs,cljg,uuid,bizPath,chuzhiren);
        }
        return result;
    }
}
