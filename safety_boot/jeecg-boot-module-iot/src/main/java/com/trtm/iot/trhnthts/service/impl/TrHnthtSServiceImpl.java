package com.trtm.iot.trhnthts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.mapper.TrHnthtSMapper;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 混凝土回弹子表
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
@Service
public class TrHnthtSServiceImpl extends ServiceImpl<TrHnthtSMapper, TrHnthtS> implements ITrHnthtSService {

    @Override
    public List<TrHnthtS> selectHntHtList(String testid) {
        try {
            QueryWrapper<TrHnthtS> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("testId",testid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
