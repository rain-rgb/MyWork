package com.trtm.iot.entityprogresscheck.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.entityprogresscheck.entity.EntityProgresscheck;
import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import com.trtm.iot.entityprogresscheck.mapper.EntityCheckDetialMapper;
import com.trtm.iot.entityprogresscheck.mapper.EntityProgresscheckMapper;
import com.trtm.iot.entityprogresscheck.service.IEntityCheckDetialService;
import com.trtm.iot.entityprogresscheck.service.IEntityProgresscheckService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 实体进度清单数据表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
@Service
public class EntityProgresscheckServiceImpl extends ServiceImpl<EntityProgresscheckMapper, EntityProgresscheck> implements IEntityProgresscheckService {

	@Autowired
	private EntityProgresscheckMapper entityProgresscheckMapper;
	@Autowired
	private EntityCheckDetialMapper entityCheckDetialMapper;
	@Autowired
	private IEntityCheckDetialService entityCheckDetialService;

	@Override
	@Transactional
	public void saveMain(EntityProgresscheck entityProgresscheck, List<EntityCheckDetial> entityCheckDetialList) {
		entityProgresscheckMapper.insert(entityProgresscheck);
		if(entityCheckDetialList!=null && entityCheckDetialList.size()>0) {
			for(EntityCheckDetial entity:entityCheckDetialList) {
				//外键设置
				entity.setUuid(entityProgresscheck.getUuid());
				if (entity.getPlanAmount()!=null&&entity.getPlannedAmount()!=null) {
				    if (Double.parseDouble(entity.getPlanAmount())!=0) {
                        entity.setPlanProgress(String.format("%.2f",
                                Double.parseDouble(entity.getPlannedAmount())/Double.parseDouble(entity.getPlanAmount())*100));
                    }
                }
				entityCheckDetialMapper.insert(entity);
			}
		}
		double remainingAmount = 0;
		double schedule = 0.0;
        double designQuantity = 0;
        QueryWrapper<EntityCheckDetial> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(planned_amount) planned_amount");
        queryWrapper.eq("uuid",entityProgresscheck.getUuid());
        EntityCheckDetial one = entityCheckDetialService.getOne(queryWrapper);
        double plannedAmount = 0;
        if (one!=null && one.getPlannedAmount()!=null){
            plannedAmount = Double.parseDouble(one.getPlannedAmount());
        }
		if (entityProgresscheck.getDesignQuantity()!=null) {
			designQuantity =Double.parseDouble(entityProgresscheck.getDesignQuantity());
		}
		remainingAmount =  designQuantity - plannedAmount;
		if (designQuantity!=0){
            schedule = plannedAmount/designQuantity*100;
        }
		EntityProgresscheck entityProgresscheck1 = new EntityProgresscheck();
		if(entityProgresscheck.getQuantityUnit().contains("m3")) {
			entityProgresscheck1.setFinishedAmount(String.format("%.1f", plannedAmount));
			entityProgresscheck1.setRemainingAmount(String.format("%.1f", remainingAmount));
		}else {
			entityProgresscheck1.setFinishedAmount(String.format("%.0f", plannedAmount));
			entityProgresscheck1.setRemainingAmount(String.format("%.0f", remainingAmount));
		}
        entityProgresscheck1.setSchedule(String.format("%.2f",schedule));
        entityProgresscheck1.setId(entityProgresscheck.getId());
        entityProgresscheckMapper.updateById(entityProgresscheck1);
	}

	@Override
	@Transactional
	public void updateMain(EntityProgresscheck entityProgresscheck,List<EntityCheckDetial> entityCheckDetialList) {
		entityProgresscheckMapper.updateById(entityProgresscheck);

		//1.先删除子表数据
		entityCheckDetialMapper.deleteByMainId(entityProgresscheck.getUuid());

		//2.子表数据重新插入
		if(entityCheckDetialList!=null && entityCheckDetialList.size()>0) {
			for(EntityCheckDetial entity:entityCheckDetialList) {
				//外键设置
				entity.setUuid(entityProgresscheck.getUuid());
				if (entity.getPlanAmount()!=null&&entity.getPlannedAmount()!=null) {
					if (Double.parseDouble(entity.getPlanAmount())!=0) {
						entity.setPlanProgress(String.format("%.2f",
								Double.parseDouble(entity.getPlannedAmount())/Double.parseDouble(entity.getPlanAmount())*100));
					}
				}
				entityCheckDetialMapper.insert(entity);
			}
		}
		double remainingAmount = 0;
		double schedule = 0.0;
		double designQuantity = 0;
		QueryWrapper<EntityCheckDetial> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("sum(planned_amount) planned_amount");
		queryWrapper.eq("uuid",entityProgresscheck.getUuid());
		EntityCheckDetial one = entityCheckDetialService.getOne(queryWrapper);
		double plannedAmount = 0;
		if (one!=null && one.getPlannedAmount()!=null){
			plannedAmount = Double.parseDouble(one.getPlannedAmount());
		}
		if (entityProgresscheck.getDesignQuantity()!=null) {
			designQuantity =Double.parseDouble(entityProgresscheck.getDesignQuantity());
		}
		remainingAmount =  designQuantity - plannedAmount;
		if (designQuantity!=0){
			schedule = plannedAmount/designQuantity*100;
		}
		EntityProgresscheck entityProgresscheck1 = new EntityProgresscheck();
		entityProgresscheck1.setFinishedAmount(String.format("%.0f",plannedAmount));
		entityProgresscheck1.setRemainingAmount(String.format("%.0f",remainingAmount));
		entityProgresscheck1.setSchedule(String.format("%.2f",schedule));
		entityProgresscheck1.setId(entityProgresscheck.getId());
		entityProgresscheckMapper.updateById(entityProgresscheck1);
	}

	@Override
	@Transactional
	public void delMain(String id) {
		entityCheckDetialMapper.deleteByMainId(id);
		entityProgresscheckMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			entityCheckDetialMapper.deleteByMainId(id.toString());
			entityProgresscheckMapper.deleteById(id);
		}
	}

}
