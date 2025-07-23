package com.trtm.iot.trkongdaogjdwm.service.impl;

import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDws;
import com.trtm.iot.trkongdaogjdwm.mapper.TrKongdaogjDwsMapper;
import com.trtm.iot.trkongdaogjdwm.service.ITrKongdaogjDwsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 孔道灌浆（定位检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@Service
public class TrKongdaogjDwsServiceImpl extends ServiceImpl<TrKongdaogjDwsMapper, TrKongdaogjDws> implements ITrKongdaogjDwsService {
	
	@Autowired
	private TrKongdaogjDwsMapper trKongdaogjDwsMapper;
	
	@Override
	public List<TrKongdaogjDws> selectByMainId(String mainId) {
		return trKongdaogjDwsMapper.selectByMainId(mainId);
	}
}
