package com.trtm.iot.hc_datalinkage.service.impl;

import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.trtm.iot.hc_datalinkage.mapper.HcDatalinkagePaveMapper;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkagePaveService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 数据联动（摊铺数据）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
@Service
public class HcDatalinkagePaveServiceImpl extends ServiceImpl<HcDatalinkagePaveMapper, HcDatalinkagePave> implements IHcDatalinkagePaveService {
	
	@Autowired
	private HcDatalinkagePaveMapper hcDatalinkagePaveMapper;
	
	@Override
	public List<HcDatalinkagePave> selectByMainId(String mainId) {
		return hcDatalinkagePaveMapper.selectByMainId(mainId);
	}
}
