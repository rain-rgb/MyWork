package com.trtm.iot.entityprogresscheck.service.impl;

import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import com.trtm.iot.entityprogresscheck.mapper.EntityCheckDetialMapper;
import com.trtm.iot.entityprogresscheck.service.IEntityCheckDetialService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 实体进度清单子表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
@Service
public class EntityCheckDetialServiceImpl extends ServiceImpl<EntityCheckDetialMapper, EntityCheckDetial> implements IEntityCheckDetialService {
	
	@Autowired
	private EntityCheckDetialMapper entityCheckDetialMapper;
	
	@Override
	public List<EntityCheckDetial> selectByMainId(String mainId) {
		return entityCheckDetialMapper.selectByMainId(mainId);
	}
}
