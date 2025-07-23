package com.trtm.iot.entityprogresscheck.service;

import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import com.trtm.iot.entityprogresscheck.entity.EntityProgresscheck;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 实体进度清单数据表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
public interface IEntityProgresscheckService extends IService<EntityProgresscheck> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(EntityProgresscheck entityProgresscheck,List<EntityCheckDetial> entityCheckDetialList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(EntityProgresscheck entityProgresscheck,List<EntityCheckDetial> entityCheckDetialList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
