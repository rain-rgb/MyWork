package com.trtm.iot.trkongdaogjdxm.service.impl;

import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxm;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxkdxx;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxs;
import com.trtm.iot.trkongdaogjdxm.mapper.TrKongdaogjDxkdxxMapper;
import com.trtm.iot.trkongdaogjdxm.mapper.TrKongdaogjDxsMapper;
import com.trtm.iot.trkongdaogjdxm.mapper.TrKongdaogjDxmMapper;
import com.trtm.iot.trkongdaogjdxm.service.ITrKongdaogjDxmService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 孔道灌浆（定性检测）主表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@Service
public class TrKongdaogjDxmServiceImpl extends ServiceImpl<TrKongdaogjDxmMapper, TrKongdaogjDxm> implements ITrKongdaogjDxmService {

	@Autowired
	private TrKongdaogjDxmMapper trKongdaogjDxmMapper;
	@Autowired
	private TrKongdaogjDxkdxxMapper trKongdaogjDxkdxxMapper;
	@Autowired
	private TrKongdaogjDxsMapper trKongdaogjDxsMapper;

	@Override
	@Transactional
	public void saveMain(TrKongdaogjDxm trKongdaogjDxm, List<TrKongdaogjDxkdxx> trKongdaogjDxkdxxList,List<TrKongdaogjDxs> trKongdaogjDxsList) {
		trKongdaogjDxmMapper.insert(trKongdaogjDxm);
		if(trKongdaogjDxkdxxList!=null && trKongdaogjDxkdxxList.size()>0) {
			for(TrKongdaogjDxkdxx entity:trKongdaogjDxkdxxList) {
				//外键设置
				entity.setZbid(trKongdaogjDxm.getId());
				trKongdaogjDxkdxxMapper.insert(entity);
			}
		}
		if(trKongdaogjDxsList!=null && trKongdaogjDxsList.size()>0) {
			for(TrKongdaogjDxs entity:trKongdaogjDxsList) {
				//外键设置
				entity.setBasicinfoid(String.valueOf(trKongdaogjDxm.getId()));
				trKongdaogjDxsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(TrKongdaogjDxm trKongdaogjDxm,List<TrKongdaogjDxkdxx> trKongdaogjDxkdxxList,List<TrKongdaogjDxs> trKongdaogjDxsList) {
		trKongdaogjDxmMapper.updateById(trKongdaogjDxm);

		//1.先删除子表数据
		trKongdaogjDxkdxxMapper.deleteByMainId(String.valueOf(trKongdaogjDxm.getId()));
		trKongdaogjDxsMapper.deleteByMainId(String.valueOf(trKongdaogjDxm.getId()));

		//2.子表数据重新插入
		if(trKongdaogjDxkdxxList!=null && trKongdaogjDxkdxxList.size()>0) {
			for(TrKongdaogjDxkdxx entity:trKongdaogjDxkdxxList) {
				//外键设置
				entity.setZbid(trKongdaogjDxm.getId());
				trKongdaogjDxkdxxMapper.insert(entity);
			}
		}
		if(trKongdaogjDxsList!=null && trKongdaogjDxsList.size()>0) {
			for(TrKongdaogjDxs entity:trKongdaogjDxsList) {
				//外键设置
				entity.setBasicinfoid(String.valueOf(trKongdaogjDxm.getId()));
				trKongdaogjDxsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		trKongdaogjDxkdxxMapper.deleteByMainId(id);
		trKongdaogjDxsMapper.deleteByMainId(id);
		trKongdaogjDxmMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			trKongdaogjDxkdxxMapper.deleteByMainId(id.toString());
			trKongdaogjDxsMapper.deleteByMainId(id.toString());
			trKongdaogjDxmMapper.deleteById(id);
		}
	}

}
