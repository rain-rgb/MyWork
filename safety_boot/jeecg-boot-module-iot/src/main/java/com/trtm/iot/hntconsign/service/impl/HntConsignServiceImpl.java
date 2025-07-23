package com.trtm.iot.hntconsign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntconsign.entity.HntConsign;
import com.trtm.iot.hntconsign.mapper.HntConsignMapper;
import com.trtm.iot.hntconsign.service.IHntConsignService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.jeecg.common.system.query.QueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 混凝土见证取样表信息
 * @Author: jeecg-boot
 * @Date: 2021-07-02
 * @Version: V1.0
 */
@Service
public class HntConsignServiceImpl extends ServiceImpl<HntConsignMapper, HntConsign> implements IHntConsignService {
    @Autowired
    HntConsignMapper hntConsignMapper;

    @Override
    public HntConsign queryGetone(String code) {
        try {
            QueryWrapper<HntConsign> hntConsignQueryWrapper = new QueryWrapper<>();
            hntConsignQueryWrapper.like("qrcode", code);
            hntConsignQueryWrapper.eq("cstatus", 0);
            return this.getOne(hntConsignQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HntConsign queryGetoneNo(String code) {
        try {
            QueryWrapper<HntConsign> hntConsignQueryWrapper = new QueryWrapper<>();
            hntConsignQueryWrapper.like("no", code);
            hntConsignQueryWrapper.eq("cstatus", 0);
            return this.getOne(hntConsignQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SysDepart> getbiaoduan() {

        return hntConsignMapper.getbiaoduan();
    }

    @Override
    public List<TSyjzb> getsyj(String arrayshebei) {

        return hntConsignMapper.getsyj(arrayshebei);
    }
}
