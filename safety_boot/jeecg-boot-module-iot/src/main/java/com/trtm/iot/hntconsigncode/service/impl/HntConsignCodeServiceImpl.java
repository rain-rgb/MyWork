package com.trtm.iot.hntconsigncode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntconsigncode.entity.HntConsignCode;
import com.trtm.iot.hntconsigncode.mapper.HntConsignCodeMapper;
import com.trtm.iot.hntconsigncode.service.IHntConsignCodeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 混凝土见证取样二维码表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Service
public class HntConsignCodeServiceImpl extends ServiceImpl<HntConsignCodeMapper, HntConsignCode> implements IHntConsignCodeService {

    @Override
    public List<HntConsignCode> selectcodelist(String wtid) {
        try{
            QueryWrapper<HntConsignCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("wtid",wtid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
