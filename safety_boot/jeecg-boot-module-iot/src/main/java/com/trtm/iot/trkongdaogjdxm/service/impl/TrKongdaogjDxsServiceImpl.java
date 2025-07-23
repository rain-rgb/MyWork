package com.trtm.iot.trkongdaogjdxm.service.impl;

import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxs;
import com.trtm.iot.trkongdaogjdxm.mapper.TrKongdaogjDxsMapper;
import com.trtm.iot.trkongdaogjdxm.service.ITrKongdaogjDxsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 孔道灌浆（定性检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@Service
public class TrKongdaogjDxsServiceImpl extends ServiceImpl<TrKongdaogjDxsMapper, TrKongdaogjDxs> implements ITrKongdaogjDxsService {
	
	@Autowired
	private TrKongdaogjDxsMapper trKongdaogjDxsMapper;
	
	@Override
	public List<TrKongdaogjDxs> selectByMainId(String mainId) {
		return trKongdaogjDxsMapper.selectByMainId(mainId);
	}
}
