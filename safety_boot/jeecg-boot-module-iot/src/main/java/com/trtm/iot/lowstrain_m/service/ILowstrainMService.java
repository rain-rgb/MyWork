package com.trtm.iot.lowstrain_m.service;

import com.trtm.iot.lowstrain_m.entity.LowstrainS;
import com.trtm.iot.lowstrain_m.entity.LowstrainM;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 低应变主表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
public interface ILowstrainMService extends IService<LowstrainM> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(LowstrainM lowstrainM,List<LowstrainS> lowstrainSList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(LowstrainM lowstrainM,List<LowstrainS> lowstrainSList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
