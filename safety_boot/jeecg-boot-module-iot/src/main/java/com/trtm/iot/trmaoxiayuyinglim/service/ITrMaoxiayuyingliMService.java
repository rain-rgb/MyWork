package com.trtm.iot.trmaoxiayuyinglim.service;

import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliS;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliM;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 锚下预应力张拉主表
 * @Author: jeecg-boot
 * @Date:   2024-03-12
 * @Version: V1.0
 */
public interface ITrMaoxiayuyingliMService extends IService<TrMaoxiayuyingliM> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(TrMaoxiayuyingliM trMaoxiayuyingliM,List<TrMaoxiayuyingliS> trMaoxiayuyingliSList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(TrMaoxiayuyingliM trMaoxiayuyingliM,List<TrMaoxiayuyingliS> trMaoxiayuyingliSList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
