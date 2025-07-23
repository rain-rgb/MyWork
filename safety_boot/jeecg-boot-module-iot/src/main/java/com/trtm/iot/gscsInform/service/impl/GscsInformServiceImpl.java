package com.trtm.iot.gscsInform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.gscsInform.entity.GscsInform;
import com.trtm.iot.gscsInform.mapper.GscsInformMapper;
import com.trtm.iot.gscsInform.service.IGscsInformService;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 班组安全管控系统通知详情表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
@Service
public class GscsInformServiceImpl extends ServiceImpl<GscsInformMapper, GscsInform> implements IGscsInformService {

    @Override
    public Integer getTestCount() {
        QueryWrapper<GscsInform> qw = new QueryWrapper<>();
        qw.eq("inform_type", 1);
        Integer count = baseMapper.selectCount(qw);
        return count;
    }

    @Override
    public String getIdByTitle(String title) {
        QueryWrapper<GscsInform> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inform_title", title);
        GscsInform gscsInform = baseMapper.selectOne(queryWrapper);
        return gscsInform.getId();
    }

    @Override
    public Integer selectCount(String title) {
        QueryWrapper<GscsInform> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inform_title", title);
        return baseMapper.selectCount(queryWrapper);
    }
}
