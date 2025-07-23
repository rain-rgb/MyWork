package com.trtm.iot.bhzSwRecipe.service;

import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.trtm.iot.bhzSwRecipe.entity.BhzSwRecipe;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;

import java.util.List;

/**
 * @Description: 水稳理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface IBhzSwRecipeService extends IService<BhzSwRecipe> {

    public void saveMain(BhzSwRecipe bhzSwRecipe, List<BhzSwPhbZibiao> bhzSwPhbZibiaoList) ;

    public void updateMain(BhzSwRecipe bhzSwRecipe, List<BhzSwPhbZibiao> bhzSwPhbZibiaoList);


}
