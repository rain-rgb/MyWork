package com.trtm.iot.zhanglass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.trtm.iot.zhanglass.mapper.TrZhanglaSSMapper;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 张拉过程子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Service
public class TrZhanglaSSServiceImpl extends ServiceImpl<TrZhanglaSSMapper, TrZhanglaSS> implements ITrZhanglaSSService {

    @Autowired TrZhanglaSSMapper trZhanglaSSMapper;
    @Override
    public List<TrZhanglaSS> getselectList(String holeid) {
//        try {
//            QueryWrapper<TrZhanglaSS> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("holeid",holeid);
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return trZhanglaSSMapper.getselectList(holeid);
    }

    @Override
    public void saveMain(List<TrZhanglaSS> trZhanglaSSList) {
        for (TrZhanglaSS trZhangla : trZhanglaSSList) {
            QueryWrapper<TrZhanglaSS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ssid",trZhangla.getSsid());
            TrZhanglaSS one = trZhanglaSSMapper.selectOne(queryWrapper);
            if (one != null){
                trZhangla.setId(one.getId());
                trZhanglaSSMapper.updateById(trZhangla);
            }else {
                trZhanglaSSMapper.insert(trZhangla);
            }
        }
    }
}
