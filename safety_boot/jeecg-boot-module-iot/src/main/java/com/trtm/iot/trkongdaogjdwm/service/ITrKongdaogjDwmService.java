package com.trtm.iot.trkongdaogjdwm.service;

import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDws;
import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDwm;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 孔道灌浆（定位检测）主表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface ITrKongdaogjDwmService extends IService<TrKongdaogjDwm> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(TrKongdaogjDwm trKongdaogjDwm,List<TrKongdaogjDws> trKongdaogjDwsList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(TrKongdaogjDwm trKongdaogjDwm,List<TrKongdaogjDws> trKongdaogjDwsList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
