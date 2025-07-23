package com.trtm.iot.bhzcfg.service;

import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 拌合站超标配置
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
public interface IBhzCallCfgService extends IService<BhzCallCfg> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(BhzCallCfg bhzCallCfg,List<BhzChaobiaoCfg> bhzChaobiaoCfgList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(BhzCallCfg bhzCallCfg,List<BhzChaobiaoCfg> bhzChaobiaoCfgList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 根据拌合站超标信息去查询相对应的报警信息
	 * @param sbjno
	 * @return
	 */
	BhzCallCfg selectbhzcallone(String sbjno);
}
