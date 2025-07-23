package com.trtm.iot.hc_transportrecords.service.impl;

import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.trtm.iot.hc_transportrecords.mapper.HcTransportrecordsPaveMapper;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsPaveService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 运输数据子表
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
@Service
public class HcTransportrecordsPaveServiceImpl extends ServiceImpl<HcTransportrecordsPaveMapper, HcTransportrecordsPave> implements IHcTransportrecordsPaveService {
	
	@Autowired
	private HcTransportrecordsPaveMapper hcTransportrecordsPaveMapper;
	
	@Override
	public List<HcTransportrecordsPave> selectByMainId(String mainId) {
		return hcTransportrecordsPaveMapper.selectByMainId(mainId);
	}
}
