package com.trtm.iot.bhzrecipe.service;

import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 砼拌合站理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
public interface IBhzRecipedetailService extends IService<BhzRecipedetail> {

	public List<BhzRecipedetail> selectByMainId(String mainId);

    int updateone(String uuid);
}
