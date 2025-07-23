package com.trtm.iot.zhanglas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.mapper.TrZhanglaSMapper;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 张拉信息子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Service
public class TrZhanglaSServiceImpl extends ServiceImpl<TrZhanglaSMapper, TrZhanglaS> implements ITrZhanglaSService {

    @Autowired
    private TrZhanglaSMapper trZhanglaSMapper;
    @Override
    public void saveMain(List<TrZhanglaS> trZhanglaS) {
        for (TrZhanglaS trZhangla : trZhanglaS) {
            QueryWrapper<TrZhanglaS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sid",trZhangla.getSid());
            TrZhanglaS trZhanglaS1 = trZhanglaSMapper.selectOne(queryWrapper);
            if (trZhanglaS1 != null){
                trZhangla.setId(trZhanglaS1.getId());
                trZhanglaSMapper.updateById(trZhangla);
            }else {
                trZhanglaSMapper.insert(trZhangla);
            }
        }
    }

    @Override
    public List<TrZhanglaS> selelctList(String syjid, String fguid) {
        try {
            QueryWrapper<TrZhanglaS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("fguid",fguid);
            queryWrapper.eq("syjid",syjid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TrZhanglaS> selelctLists(String syjid, String fguid,int i) {
        try {
            QueryWrapper<TrZhanglaS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("fguid",fguid);
            queryWrapper.eq("syjid",syjid);
            queryWrapper.eq("jdbfb",50);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TrZhanglaS> selelctListbybd(String syjid, String fguid,int i) {
        try {
            QueryWrapper<TrZhanglaS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("fguid",fguid);
            queryWrapper.eq("syjid",syjid);
            queryWrapper.eq("dh",i);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
