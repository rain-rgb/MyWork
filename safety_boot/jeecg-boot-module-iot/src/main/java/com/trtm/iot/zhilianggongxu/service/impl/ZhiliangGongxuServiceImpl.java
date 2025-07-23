package com.trtm.iot.zhilianggongxu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.mapper.ZhiliangGongxuMapper;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangtaizuocfg.vo.tjVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 制梁工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-22
 * @Version: V1.0
 */
@Service
public class ZhiliangGongxuServiceImpl extends ServiceImpl<ZhiliangGongxuMapper, ZhiliangGongxu> implements IZhiliangGongxuService {

    @Autowired
    ZhiliangGongxuMapper zhiliangGongxuMapper;

    @Override
    public List<ZhiliangGongxu> selectByMainId(String id) {
        try {
            QueryWrapper<ZhiliangGongxu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid",id);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateones(String uuid) {
        return zhiliangGongxuMapper.updateone(uuid);
    }

    @Override
    public ZhiliangGongxu selectuuid(String uuid, int xuhao) {
//        try {
//            QueryWrapper<ZhiliangGongxu> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("uuid",uuid);
//            queryWrapper.eq("xuhao",xuhao);
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return zhiliangGongxuMapper.selectuuid(uuid,xuhao);
    }

    @Override
    public List<ZhiliangGongxu> selectgongxu(String uuid) {
        return zhiliangGongxuMapper.selectgongxu(uuid);
    }

    @Override
    public List<ZhiliangGongxu> selectgongxuPinmin(String uuid) {
        return zhiliangGongxuMapper.selectgongxuPinmin(uuid);
    }

    @Override
    public List<ZhiliangGongxu> selectuuidlist(String uuid, Integer xuhao) {
        return zhiliangGongxuMapper.selectuuidlist(uuid,xuhao);
    }

}
