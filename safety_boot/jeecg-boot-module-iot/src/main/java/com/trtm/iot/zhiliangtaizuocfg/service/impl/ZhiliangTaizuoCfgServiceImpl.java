package com.trtm.iot.zhiliangtaizuocfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.mapper.ZhiliangTaizuoCfgMapper;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 制梁台座配置表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
@Service
public class ZhiliangTaizuoCfgServiceImpl extends ServiceImpl<ZhiliangTaizuoCfgMapper, ZhiliangTaizuoCfg> implements IZhiliangTaizuoCfgService {

    @Autowired ZhiliangTaizuoCfgMapper zhiliangTaizuoCfgMapper;
    @Override
    public ZhiliangTaizuoCfg selectzltaizuone() {
        try {
            QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ZhiliangTaizuoCfg selectzltaizuo(String taizuono) {
//        try {
//            QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("taizuono",taizuono);
//            queryWrapper.eq("status",0);
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return zhiliangTaizuoCfgMapper.selectzltaizuo(taizuono);
    }

    @Override
    public ZhiliangTaizuoCfg selectzltaizuos(String taizuono) {
        try {
            QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("taizuono",taizuono);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
