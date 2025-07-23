package com.trtm.iot.bhzSwRecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.trtm.iot.bhzSwPhbZibiao.mapper.BhzSwPhbZibiaoMapper;
import com.trtm.iot.bhzSwPhbZibiao.service.IBhzSwPhbZibiaoService;
import com.trtm.iot.bhzSwRecipe.entity.BhzSwRecipe;
import com.trtm.iot.bhzSwRecipe.mapper.BhzSwRecipeMapper;
import com.trtm.iot.bhzSwRecipe.service.IBhzSwRecipeService;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.trtm.iot.lqbhzrecipe.mapper.BhzLqPhbZibiaoMapper;
import com.trtm.iot.lqbhzrecipe.mapper.BhzLqRecipeMapper;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqPhbZibiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 水稳理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Service
public class BhzSwRecipeServiceImpl extends ServiceImpl<BhzSwRecipeMapper, BhzSwRecipe> implements IBhzSwRecipeService {

    @Autowired
    private BhzSwRecipeMapper bhzSwRecipeMapper;
    @Autowired
    private BhzSwPhbZibiaoMapper bhzSwPhbZibiaoMapper;

    @Autowired
    private IBhzSwPhbZibiaoService iBhzLqSwPhbZibiaoService;

    @Override
    public void saveMain(BhzSwRecipe bhzSwRecipe, List<BhzSwPhbZibiao> bhzSwPhbZibiaoList) {
        bhzSwRecipeMapper.insert(bhzSwRecipe);
        if(bhzSwPhbZibiaoList!=null && bhzSwPhbZibiaoList.size()>0) {
            for(BhzSwPhbZibiao entity:bhzSwPhbZibiaoList) {
                //外键设置
                entity.setZhuziid(bhzSwRecipe.getZhuziid());
                entity.setId(null);
                bhzSwPhbZibiaoMapper.insert(entity);
            }
        }
    }

    @Override
    public void updateMain(BhzSwRecipe bhzSwRecipe, List<BhzSwPhbZibiao> bhzSwPhbZibiaoList) {
        bhzSwRecipeMapper.updateById(bhzSwRecipe);

        //1.先删除子表数据
        QueryWrapper<BhzSwPhbZibiao> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("zhuziid",bhzSwRecipe.getZhuziid());
        List<BhzSwPhbZibiao> list = iBhzLqSwPhbZibiaoService.list(queryWrapper);
        if(list.size()>0){
            for (BhzSwPhbZibiao bhzLqPhbZibiao : list) {
                bhzSwPhbZibiaoMapper.deleteById(bhzLqPhbZibiao.getId());
            }
        }
        //2.子表数据重新插入
        if(bhzSwPhbZibiaoList!=null && bhzSwPhbZibiaoList.size()>0) {
            for(BhzSwPhbZibiao entity:bhzSwPhbZibiaoList) {
                //外键设置
                entity.setZhuziid(bhzSwRecipe.getZhuziid());
                bhzSwPhbZibiaoMapper.insert(entity);
            }
        }
    }


}
