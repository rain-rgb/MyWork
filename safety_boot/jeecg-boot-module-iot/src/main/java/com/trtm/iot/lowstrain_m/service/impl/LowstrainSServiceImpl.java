package com.trtm.iot.lowstrain_m.service.impl;

import com.trtm.iot.lowstrain_m.entity.LowstrainS;
import com.trtm.iot.lowstrain_m.mapper.LowstrainSMapper;
import com.trtm.iot.lowstrain_m.service.ILowstrainSService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 低应变子表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
@Service
public class LowstrainSServiceImpl extends ServiceImpl<LowstrainSMapper, LowstrainS> implements ILowstrainSService {
	
	@Autowired
	private LowstrainSMapper lowstrainSMapper;
	
	@Override
	public List<LowstrainS> selectByMainId(String mainId) {
		return lowstrainSMapper.selectByMainId(mainId);
	}
}
