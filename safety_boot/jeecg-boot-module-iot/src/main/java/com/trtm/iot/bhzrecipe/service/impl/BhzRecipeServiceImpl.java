package com.trtm.iot.bhzrecipe.service.impl;

import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.bhzrecipe.entity.BhzRecipepb;
import com.trtm.iot.bhzrecipe.mapper.BhzRecipedetailMapper;
import com.trtm.iot.bhzrecipe.mapper.BhzRecipeMapper;
import com.trtm.iot.bhzrecipe.service.IBhzRecipeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.UUID;

/**
 * @Description: 砼拌合站理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
@Service
public class BhzRecipeServiceImpl extends ServiceImpl<BhzRecipeMapper, BhzRecipe> implements IBhzRecipeService {

	@Autowired
	private BhzRecipeMapper bhzRecipeMapper;
	@Autowired
	private BhzRecipedetailMapper bhzRecipedetailMapper;

	@Override
	@Transactional
	public void saveMain(BhzRecipe bhzRecipe, List<BhzRecipedetail> bhzRecipedetailList) {
		bhzRecipeMapper.insert(bhzRecipe);
		if(bhzRecipedetailList!=null && bhzRecipedetailList.size()>0) {
			for(BhzRecipedetail entity:bhzRecipedetailList) {
				//外键设置
				entity.setRecipeid(bhzRecipe.getUuid());
				String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
				entity.setOrigid(uuid);
				entity.setId(null);
				bhzRecipedetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(BhzRecipe bhzRecipe,List<BhzRecipedetail> bhzRecipedetailList) {
		bhzRecipeMapper.updateById(bhzRecipe);

		//1.先删除子表数据
		bhzRecipedetailMapper.deleteByMainId(bhzRecipe.getUuid());

		//2.子表数据重新插入
		if(bhzRecipedetailList!=null && bhzRecipedetailList.size()>0) {
			for(BhzRecipedetail entity:bhzRecipedetailList) {
				//外键设置
				entity.setRecipeid(bhzRecipe.getUuid());
				entity.setOrigid(bhzRecipe.getUuid());
				bhzRecipedetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		bhzRecipedetailMapper.deleteByMainId(id);
		bhzRecipeMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			bhzRecipedetailMapper.deleteByMainId(id.toString());
			bhzRecipeMapper.deleteById(id);
		}
	}

	@Override
	public List<BhzRecipepb> getPhb() {
		return bhzRecipedetailMapper.getPhb();
	}
}
