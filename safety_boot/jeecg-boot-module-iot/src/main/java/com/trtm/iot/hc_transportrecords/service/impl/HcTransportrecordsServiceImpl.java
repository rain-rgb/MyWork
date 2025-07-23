package com.trtm.iot.hc_transportrecords.service.impl;

import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.trtm.iot.hc_transportrecords.mapper.HcTransportrecordsPaveMapper;
import com.trtm.iot.hc_transportrecords.mapper.HcTransportrecordsMapper;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 运输数据
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
@Service
public class HcTransportrecordsServiceImpl extends ServiceImpl<HcTransportrecordsMapper, HcTransportrecords> implements IHcTransportrecordsService {

	@Autowired
	private HcTransportrecordsMapper hcTransportrecordsMapper;
	@Autowired
	private HcTransportrecordsPaveMapper hcTransportrecordsPaveMapper;

	@Override
	@Transactional
	public void saveMain(HcTransportrecords hcTransportrecords, List<HcTransportrecordsPave> hcTransportrecordsPaveList) {
		hcTransportrecordsMapper.insert(hcTransportrecords);
		if(hcTransportrecordsPaveList!=null && hcTransportrecordsPaveList.size()>0) {
			for(HcTransportrecordsPave entity:hcTransportrecordsPaveList) {
				//外键设置
				entity.setZbid(hcTransportrecords.getId());
				hcTransportrecordsPaveMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(HcTransportrecords hcTransportrecords,List<HcTransportrecordsPave> hcTransportrecordsPaveList) {
		hcTransportrecordsMapper.updateById(hcTransportrecords);

		//1.先删除子表数据
		hcTransportrecordsPaveMapper.deleteByMainId(String.valueOf(hcTransportrecords.getId()));

		//2.子表数据重新插入
		if(hcTransportrecordsPaveList!=null && hcTransportrecordsPaveList.size()>0) {
			for(HcTransportrecordsPave entity:hcTransportrecordsPaveList) {
				//外键设置
				entity.setZbid(hcTransportrecords.getId());
				hcTransportrecordsPaveMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		hcTransportrecordsPaveMapper.deleteByMainId(id);
		hcTransportrecordsMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			hcTransportrecordsPaveMapper.deleteByMainId(id.toString());
			hcTransportrecordsMapper.deleteById(id);
		}
	}

	@Override
	public List<HcTransportrecords> getList(String shebeilist, Integer curid) {
		return hcTransportrecordsPaveMapper.getList(shebeilist,curid);
	}

	@Override
	public List<HcTransportrecords> getListjt(String shebeilist, Integer curid) {
		return hcTransportrecordsPaveMapper.getListjt(shebeilist,curid);
	}
}
