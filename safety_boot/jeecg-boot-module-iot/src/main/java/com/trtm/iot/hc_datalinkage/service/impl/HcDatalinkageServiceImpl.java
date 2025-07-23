package com.trtm.iot.hc_datalinkage.service.impl;

import com.trtm.iot.hc_datalinkage.entity.HcDatalinkage;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.trtm.iot.hc_datalinkage.mapper.HcDatalinkagePaveMapper;
import com.trtm.iot.hc_datalinkage.mapper.HcDatalinkageMapper;
import com.trtm.iot.hc_datalinkage.service.IHcDatalinkageService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 数据联动（运输信息）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
@Service
public class HcDatalinkageServiceImpl extends ServiceImpl<HcDatalinkageMapper, HcDatalinkage> implements IHcDatalinkageService {

	@Autowired
	private HcDatalinkageMapper hcDatalinkageMapper;
	@Autowired
	private HcDatalinkagePaveMapper hcDatalinkagePaveMapper;

	@Override
	@Transactional
	public void saveMain(HcDatalinkage hcDatalinkage, List<HcDatalinkagePave> hcDatalinkagePaveList) {
		hcDatalinkageMapper.insert(hcDatalinkage);
		if(hcDatalinkagePaveList!=null && hcDatalinkagePaveList.size()>0) {
			for(HcDatalinkagePave entity:hcDatalinkagePaveList) {
				//外键设置
				entity.setZbid(hcDatalinkage.getId());
				hcDatalinkagePaveMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(HcDatalinkage hcDatalinkage,List<HcDatalinkagePave> hcDatalinkagePaveList) {
		hcDatalinkageMapper.updateById(hcDatalinkage);

		//1.先删除子表数据
		hcDatalinkagePaveMapper.deleteByMainId(String.valueOf(hcDatalinkage.getId()));

		//2.子表数据重新插入
		if(hcDatalinkagePaveList!=null && hcDatalinkagePaveList.size()>0) {
			for(HcDatalinkagePave entity:hcDatalinkagePaveList) {
				//外键设置
				entity.setZbid(hcDatalinkage.getId());
				hcDatalinkagePaveMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		hcDatalinkagePaveMapper.deleteByMainId(id);
		hcDatalinkageMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			hcDatalinkagePaveMapper.deleteByMainId(id.toString());
			hcDatalinkageMapper.deleteById(id);
		}
	}

	@Override
	public List<HcDatalinkage> getList(String shebeilist, Integer curid) {
		return hcDatalinkageMapper.getList(shebeilist,curid);
	}

	@Override
	public List<HcDatalinkage> getListjt(String shebeilist, Integer curid) {
		return hcDatalinkageMapper.getListjt(shebeilist,curid);
	}
}
