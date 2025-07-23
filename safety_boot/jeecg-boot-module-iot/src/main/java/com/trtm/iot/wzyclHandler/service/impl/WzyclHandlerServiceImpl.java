package com.trtm.iot.wzyclHandler.service.impl;

import com.trtm.iot.wzyclHandler.entity.WzyclHandler;
import com.trtm.iot.wzyclHandler.mapper.WzyclHandlerMapper;
import com.trtm.iot.wzyclHandler.service.IWzyclHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: wzycl_handler
 * @Author: jeecg-boot
 * @Date: 2022-11-21
 * @Version: V1.0
 */
@Service
public class WzyclHandlerServiceImpl extends ServiceImpl<WzyclHandlerMapper, WzyclHandler> implements IWzyclHandlerService {

    @Autowired
    private WzyclHandlerMapper wzyclHandlerMapper;

    @Override
    public int handlerInfoAddOrUpdate(String baseid, String wtyy, String czfs, String czjg, String czperson, String czfile) {
        String s = wzyclHandlerMapper.selectByBaseId(baseid);
        int result = 0;
        if (s == null) {
            result = wzyclHandlerMapper.addCZInfoById(baseid, wtyy, czfs, czjg, czperson, czfile);
        } else {
            result = wzyclHandlerMapper.updateCZInfoById(wtyy, czfs, czjg, czperson, czfile, baseid);
        }
        return result;
    }

    @Override
    public int superviseInfoAddOrUpdate(String baseid, String spyj, String spjg, String spperson, String spfile) {
        String s = wzyclHandlerMapper.selectByBaseId(baseid);
        int result = 0;
        if (s == null) {
            result = wzyclHandlerMapper.addSPInfoById(baseid, spyj, spjg, spperson, spfile);
        } else {
            result = wzyclHandlerMapper.updateSPInfoById(spyj, spjg, spperson, spfile, baseid);
        }
        return result;
    }

}
