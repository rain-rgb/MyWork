package com.trtm.iot.lqbhzrecipe.mapper;

import java.util.List;

import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: bhz_lq_recipe
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface BhzLqRecipeMapper extends BaseMapper<BhzLqRecipe> {
    /**
     *
     */
    @Select("select llysb from bhz_lq_recipe where shebeibianhao=#{shebeibianhao} and llmoren=1" +
            " and llysb>#{youshibi1} and llysb<#{youshibi2} limit 1")
    BhzLqPhbZibiao selectllysb(String shebeibianhao, String youshibi1 , String youshibi2);

    // @Select("select GROUP_CONCAT( CONCAT(\"'\",phbid,\"'\") SEPARATOR ',')  from bhz_lq_recipe where departid LIKE  CONCAT(#{code},\"%\") ")
    @Select("select GROUP_CONCAT( phbid SEPARATOR ',')  from bhz_lq_recipe where departid LIKE  CONCAT(#{code},\"%\") ")
    String getPhbidsByCode(String code);
}
