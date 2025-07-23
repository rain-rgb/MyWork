package com.trtm.iot.lqbhzrecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.trtm.iot.lqbhzrecipe.mapper.BhzLqPhbZibiaoMapper;
import com.trtm.iot.lqbhzrecipe.mapper.BhzLqRecipeMapper;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqPhbZibiaoService;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.UUID;

/**
 * @Description: bhz_lq_recipe
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzLqRecipeServiceImpl extends ServiceImpl<BhzLqRecipeMapper, BhzLqRecipe> implements IBhzLqRecipeService {
    @Autowired
    private BhzLqRecipeMapper bhzLqRecipeMapper;
    @Autowired
    private BhzLqPhbZibiaoMapper bhzLqPhbZibiaoMapper;
    @Autowired
    private IBhzLqPhbZibiaoService iBhzLqPhbZibiaoService;
    @Override
    public BhzLqPhbZibiao selectllysb(String shebeibianhao, String youshibi1, String youshibi2) {
        return  bhzLqRecipeMapper.selectllysb(shebeibianhao,youshibi1,youshibi2);
    }

    @Override
    public void saveMain(BhzLqRecipe bhzLqRecipe, List<BhzLqPhbZibiao> bhzLqPhbZibiaoList) {
        bhzLqRecipeMapper.insert(bhzLqRecipe);
        if(bhzLqPhbZibiaoList!=null && bhzLqPhbZibiaoList.size()>0) {
            for(BhzLqPhbZibiao entity:bhzLqPhbZibiaoList) {
                //外键设置
                entity.setZhuziid(bhzLqRecipe.getZhuziid());
                entity.setId(null);
                bhzLqPhbZibiaoMapper.insert(entity);
            }
        }
    }

    @Override
    public void updateMain(BhzLqRecipe bhzLqRecipe, List<BhzLqPhbZibiao> bhzLqPhbZibiaoList) {
        bhzLqRecipeMapper.updateById(bhzLqRecipe);

        //1.先删除子表数据
        QueryWrapper<BhzLqPhbZibiao> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("zhuziid",bhzLqRecipe.getZhuziid());
        List<BhzLqPhbZibiao> list = iBhzLqPhbZibiaoService.list(queryWrapper);
        if(list.size()>0){
            for (BhzLqPhbZibiao bhzLqPhbZibiao : list) {
                bhzLqPhbZibiaoMapper.deleteById(bhzLqPhbZibiao.getId());
            }
        }
        //2.子表数据重新插入
        if(bhzLqPhbZibiaoList!=null && bhzLqPhbZibiaoList.size()>0) {
            for(BhzLqPhbZibiao entity:bhzLqPhbZibiaoList) {
                //外键设置
                entity.setZhuziid(bhzLqRecipe.getZhuziid());
                bhzLqPhbZibiaoMapper.insert(entity);
            }
        }
    }

    @Override
    public String getPhbidsByCode(String code) {
        return bhzLqRecipeMapper.getPhbidsByCode(code);
    }
}
