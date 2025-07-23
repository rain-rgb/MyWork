package com.trtm.iot.bhzrecipe.service;

import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.bhzrecipe.entity.BhzRecipepb;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 砼拌合站理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
public interface IBhzRecipeService extends IService<BhzRecipe> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(BhzRecipe bhzRecipe,List<BhzRecipedetail> bhzRecipedetailList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(BhzRecipe bhzRecipe,List<BhzRecipedetail> bhzRecipedetailList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

    List<BhzRecipepb> getPhb();
}
