package com.trtm.iot.lowstrain_m.service.impl;

import com.trtm.iot.lowstrain_m.entity.LowstrainM;
import com.trtm.iot.lowstrain_m.entity.LowstrainS;
import com.trtm.iot.lowstrain_m.mapper.LowstrainSMapper;
import com.trtm.iot.lowstrain_m.mapper.LowstrainMMapper;
import com.trtm.iot.lowstrain_m.service.ILowstrainMService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 低应变主表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
@Service
public class LowstrainMServiceImpl extends ServiceImpl<LowstrainMMapper, LowstrainM> implements ILowstrainMService {

	@Autowired
	private LowstrainMMapper lowstrainMMapper;
	@Autowired
	private LowstrainSMapper lowstrainSMapper;
	
	@Override
	@Transactional
	public void saveMain(LowstrainM lowstrainM, List<LowstrainS> lowstrainSList) {
		lowstrainMMapper.insert(lowstrainM);
		if(lowstrainSList!=null && lowstrainSList.size()>0) {
			for(LowstrainS entity:lowstrainSList) {
				//外键设置
				entity.setBaseinfoid(lowstrainM.getId());
				lowstrainSMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(LowstrainM lowstrainM,List<LowstrainS> lowstrainSList) {
		lowstrainMMapper.updateById(lowstrainM);
		
		//1.先删除子表数据
		lowstrainSMapper.deleteByMainId(lowstrainM.getId());
		
		//2.子表数据重新插入
		if(lowstrainSList!=null && lowstrainSList.size()>0) {
			for(LowstrainS entity:lowstrainSList) {
				//外键设置
				entity.setBaseinfoid(lowstrainM.getId());
				lowstrainSMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		lowstrainSMapper.deleteByMainId(id);
		lowstrainMMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			lowstrainSMapper.deleteByMainId(id.toString());
			lowstrainMMapper.deleteById(id);
		}
	}
	
}
