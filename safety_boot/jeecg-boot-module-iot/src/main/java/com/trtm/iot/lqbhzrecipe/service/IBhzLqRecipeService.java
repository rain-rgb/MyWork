package com.trtm.iot.lqbhzrecipe.service;

import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: bhz_lq_recipe
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzLqRecipeService extends IService<BhzLqRecipe> {
    BhzLqPhbZibiao selectllysb(String shebeibianhao, String youshibi1 , String youshibi2);

    /**
     * 添加一对多
     *
     */
    public void saveMain(BhzLqRecipe bhzLqRecipe, List<BhzLqPhbZibiao> bhzLqPhbZibiaoList) ;
    /**
     * 修改一对多
     *
     */
    public void updateMain(BhzLqRecipe bhzLqRecipe,List<BhzLqPhbZibiao> bhzLqPhbZibiaoList);

    String getPhbidsByCode(String code);
}
