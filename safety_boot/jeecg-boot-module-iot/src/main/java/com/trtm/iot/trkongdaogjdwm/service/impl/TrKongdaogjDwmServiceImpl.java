package com.trtm.iot.trkongdaogjdwm.service.impl;

import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDwm;
import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDws;
import com.trtm.iot.trkongdaogjdwm.mapper.TrKongdaogjDwsMapper;
import com.trtm.iot.trkongdaogjdwm.mapper.TrKongdaogjDwmMapper;
import com.trtm.iot.trkongdaogjdwm.service.ITrKongdaogjDwmService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 孔道灌浆（定位检测）主表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@Service
public class TrKongdaogjDwmServiceImpl extends ServiceImpl<TrKongdaogjDwmMapper, TrKongdaogjDwm> implements ITrKongdaogjDwmService {

	@Autowired
	private TrKongdaogjDwmMapper trKongdaogjDwmMapper;
	@Autowired
	private TrKongdaogjDwsMapper trKongdaogjDwsMapper;

	@Override
	@Transactional
	public void saveMain(TrKongdaogjDwm trKongdaogjDwm, List<TrKongdaogjDws> trKongdaogjDwsList) {
		trKongdaogjDwmMapper.insert(trKongdaogjDwm);
		if(trKongdaogjDwsList!=null && trKongdaogjDwsList.size()>0) {
			for(TrKongdaogjDws entity:trKongdaogjDwsList) {
				//外键设置
				entity.setBasicinfoid(String.valueOf(trKongdaogjDwm.getId()));
				trKongdaogjDwsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(TrKongdaogjDwm trKongdaogjDwm,List<TrKongdaogjDws> trKongdaogjDwsList) {
		trKongdaogjDwmMapper.updateById(trKongdaogjDwm);

		//1.先删除子表数据
		trKongdaogjDwsMapper.deleteByMainId(String.valueOf(trKongdaogjDwm.getId()));

		//2.子表数据重新插入
		if(trKongdaogjDwsList!=null && trKongdaogjDwsList.size()>0) {
			for(TrKongdaogjDws entity:trKongdaogjDwsList) {
				//外键设置
				entity.setBasicinfoid(String.valueOf(trKongdaogjDwm.getId()));
				trKongdaogjDwsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		trKongdaogjDwsMapper.deleteByMainId(id);
		trKongdaogjDwmMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			trKongdaogjDwsMapper.deleteByMainId(id.toString());
			trKongdaogjDwmMapper.deleteById(id);
		}
	}

}
