package com.trtm.iot.bhzrecipe.service.impl;

import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.bhzrecipe.mapper.BhzRecipedetailMapper;
import com.trtm.iot.bhzrecipe.service.IBhzRecipedetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 砼拌合站理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
@Service
public class BhzRecipedetailServiceImpl extends ServiceImpl<BhzRecipedetailMapper, BhzRecipedetail> implements IBhzRecipedetailService {

	@Autowired
	private BhzRecipedetailMapper bhzRecipedetailMapper;

	@Override
	public List<BhzRecipedetail> selectByMainId(String mainId) {
		return bhzRecipedetailMapper.selectByMainId(mainId);
	}
	@Override
	public int updateone(String uuid){
		return bhzRecipedetailMapper.updateone(uuid);
	}
}
