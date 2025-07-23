package com.trtm.iot.kongdaoyaj.service.impl;

import com.trtm.iot.kongdaoyaj.entity.Kongdaoyaj;
import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import com.trtm.iot.kongdaoyaj.mapper.KongdaoyajsMapper;
import com.trtm.iot.kongdaoyaj.mapper.KongdaoyajMapper;
import com.trtm.iot.kongdaoyaj.service.IKongdaoyajService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 孔道灌浆主表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
@Service
public class KongdaoyajServiceImpl extends ServiceImpl<KongdaoyajMapper, Kongdaoyaj> implements IKongdaoyajService {

	@Autowired
	private KongdaoyajMapper kongdaoyajMapper;
	@Autowired
	private KongdaoyajsMapper kongdaoyajsMapper;

	@Override
	@Transactional
	public void saveMain(Kongdaoyaj kongdaoyaj, List<Kongdaoyajs> kongdaoyajsList) {
		kongdaoyajMapper.insert(kongdaoyaj);
		if(kongdaoyajsList!=null && kongdaoyajsList.size()>0) {
			for(Kongdaoyajs entity:kongdaoyajsList) {
				//外键设置
				entity.setUuid(kongdaoyaj.getUuid());
				kongdaoyajsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(Kongdaoyaj kongdaoyaj,List<Kongdaoyajs> kongdaoyajsList) {
		kongdaoyajMapper.updateById(kongdaoyaj);

		//1.先删除子表数据
		kongdaoyajsMapper.deleteByMainId(String.valueOf(kongdaoyaj.getId()));

		//2.子表数据重新插入
		if(kongdaoyajsList!=null && kongdaoyajsList.size()>0) {
			for(Kongdaoyajs entity:kongdaoyajsList) {
				//外键设置
				entity.setUuid(kongdaoyaj.getUuid());
				kongdaoyajsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		kongdaoyajsMapper.deleteByMainId(id);
		kongdaoyajMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			kongdaoyajsMapper.deleteByMainId(id.toString());
			kongdaoyajMapper.deleteById(id);
		}
	}

}
