package com.trtm.iot.bhzcfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.mapper.BhzChaobiaoCfgMapper;
import com.trtm.iot.bhzcfg.mapper.BhzCallCfgMapper;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.util.uuidUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 拌合站超标配置
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Service
public class BhzCallCfgServiceImpl extends ServiceImpl<BhzCallCfgMapper, BhzCallCfg> implements IBhzCallCfgService {

	@Autowired
	private BhzCallCfgMapper bhzCallCfgMapper;
	@Autowired
	private BhzChaobiaoCfgMapper bhzChaobiaoCfgMapper;

	@Override
	@Transactional
	public void saveMain(BhzCallCfg bhzCallCfg, List<BhzChaobiaoCfg> bhzChaobiaoCfgList) {
		bhzCallCfg.setUid(uuidUtil.getUUID32());
		bhzCallCfgMapper.insert(bhzCallCfg);
		if(bhzChaobiaoCfgList!=null && bhzChaobiaoCfgList.size()>0) {
			for(BhzChaobiaoCfg entity:bhzChaobiaoCfgList) {
				//外键设置
				entity.setUid(bhzCallCfg.getUid());
				entity.setBhjno(bhzCallCfg.getBhjno());
				entity.setId(null);
				bhzChaobiaoCfgMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(BhzCallCfg bhzCallCfg,List<BhzChaobiaoCfg> bhzChaobiaoCfgList) {
		bhzCallCfgMapper.updateById(bhzCallCfg);

		//1.先删除子表数据
		bhzChaobiaoCfgMapper.deleteByMainId(bhzCallCfg.getUid());

		//2.子表数据重新插入
		if(bhzChaobiaoCfgList!=null && bhzChaobiaoCfgList.size()>0) {
			for(BhzChaobiaoCfg entity:bhzChaobiaoCfgList) {
				//外键设置
				entity.setUid(bhzCallCfg.getUid());
				entity.setBhjno(bhzCallCfg.getBhjno());
				bhzChaobiaoCfgMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		bhzChaobiaoCfgMapper.deleteByMainId(id);
		bhzCallCfgMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			bhzChaobiaoCfgMapper.deleteByMainId(id.toString());
			bhzCallCfgMapper.deleteById(id);
		}
	}
	@Override
	public BhzCallCfg selectbhzcallone(String sbjno){
		try {
			QueryWrapper<BhzCallCfg> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq("bhjno",sbjno);
			return this.getOne(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}

}
