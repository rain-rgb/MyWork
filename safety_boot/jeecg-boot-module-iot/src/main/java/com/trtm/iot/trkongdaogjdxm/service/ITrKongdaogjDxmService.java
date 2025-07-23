package com.trtm.iot.trkongdaogjdxm.service;

import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxkdxx;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxs;
import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxm;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 孔道灌浆（定性检测）主表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface ITrKongdaogjDxmService extends IService<TrKongdaogjDxm> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(TrKongdaogjDxm trKongdaogjDxm,List<TrKongdaogjDxkdxx> trKongdaogjDxkdxxList,List<TrKongdaogjDxs> trKongdaogjDxsList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(TrKongdaogjDxm trKongdaogjDxm,List<TrKongdaogjDxkdxx> trKongdaogjDxkdxxList,List<TrKongdaogjDxs> trKongdaogjDxsList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
