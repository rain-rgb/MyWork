package com.trtm.iot.bhzcfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.mapper.BhzPhoneMapper;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 拌合站手机号码配置
 * @Author: jeecg-boot
 * @Date: 2021-03-19
 * @Version: V1.0
 */
@Service
public class BhzPhoneServiceImpl extends ServiceImpl<BhzPhoneMapper, BhzPhone> implements IBhzPhoneService {

    /**
     * 根据拌合站超标配置去查询相对应的手机号码
     *
     * @param uid
     * @return
     */
    @Override
    public BhzPhone selectBhzPhone(String uid) {
        try {
            QueryWrapper<BhzPhone> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", uid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzPhone> selectBhzPhoneList(String sysorgcode, Integer phonesname) {
        try {
            QueryWrapper<BhzPhone> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sys_org_code", sysorgcode);
            queryWrapper.eq("phonesname", phonesname);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BhzPhone getBhzPhone(String names) {
        QueryWrapper<BhzPhone> bhzPhoneQueryWrapper = new QueryWrapper<>();
        bhzPhoneQueryWrapper.eq("names", names);
        bhzPhoneQueryWrapper.eq("phonesname",24);
        return this.getOne(bhzPhoneQueryWrapper);
    }
}
