package com.trtm.iot.bhzrecipe.mapper;

import java.util.List;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.bhzrecipe.entity.BhzRecipepb;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 砼拌合站理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
public interface BhzRecipedetailMapper extends BaseMapper<BhzRecipedetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	@Select("select * from bhz_recipedetail where recipeId =#{mainId}")
	public List<BhzRecipedetail> selectByMainId(@Param("mainId") String mainId);

	@Update("update bhz_recipedetail set isdel = 1 where recipeId =#{uuid}")
	int updateone(String uuid);

    List<BhzRecipepb> getPhb();
}
