package com.trtm.iot.trkongdaogjdxm.service.impl;

import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxkdxx;
import com.trtm.iot.trkongdaogjdxm.mapper.TrKongdaogjDxkdxxMapper;
import com.trtm.iot.trkongdaogjdxm.service.ITrKongdaogjDxkdxxService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 孔道灌浆（定性检测）孔道信息表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@Service
public class TrKongdaogjDxkdxxServiceImpl extends ServiceImpl<TrKongdaogjDxkdxxMapper, TrKongdaogjDxkdxx> implements ITrKongdaogjDxkdxxService {
	
	@Autowired
	private TrKongdaogjDxkdxxMapper trKongdaogjDxkdxxMapper;
	
	@Override
	public List<TrKongdaogjDxkdxx> selectByMainId(String mainId) {
		return trKongdaogjDxkdxxMapper.selectByMainId(mainId);
	}
}
