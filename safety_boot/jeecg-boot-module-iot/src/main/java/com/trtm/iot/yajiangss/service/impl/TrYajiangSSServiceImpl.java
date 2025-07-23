package com.trtm.iot.yajiangss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import com.trtm.iot.yajiangss.mapper.TrYajiangSSMapper;
import com.trtm.iot.yajiangss.service.ITrYajiangSSService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.mapper.TrZhanglaSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 压浆过程表
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
@Service
public class TrYajiangSSServiceImpl extends ServiceImpl<TrYajiangSSMapper, TrYajiangSS> implements ITrYajiangSSService {
    @Autowired
    private TrYajiangSSMapper trYajiangSSMapper;
    @Autowired
    private ITrYajiangSSService trYajiangSSService;

    @Override
    public List<TrYajiangSS> selectList(String holeid) {
        try {
            QueryWrapper<TrYajiangSS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("holeid",holeid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveMain(List<TrYajiangSS> trYajiangSS) {
        for (TrYajiangSS trYajiangSS1 : trYajiangSS) {
            TrYajiangSS trYajiangSS2 = trYajiangSSService.selelctone(trYajiangSS1.getSid());
            if (trYajiangSS2==null) {
                trYajiangSSMapper.insert(trYajiangSS1);
            }else {
                continue;
            }
        }
    }

    @Override
    public TrYajiangSS selelctone(String sid) {
        try {
            QueryWrapper<TrYajiangSS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sid",sid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
