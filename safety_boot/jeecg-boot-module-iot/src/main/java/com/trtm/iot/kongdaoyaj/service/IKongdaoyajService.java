package com.trtm.iot.kongdaoyaj.service;

import com.trtm.iot.kongdaoyaj.entity.Kongdaoyajs;
import com.trtm.iot.kongdaoyaj.entity.Kongdaoyaj;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 孔道灌浆主表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
public interface IKongdaoyajService extends IService<Kongdaoyaj> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(Kongdaoyaj kongdaoyaj,List<Kongdaoyajs> kongdaoyajsList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(Kongdaoyaj kongdaoyaj,List<Kongdaoyajs> kongdaoyajsList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
