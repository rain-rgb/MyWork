package com.trtm.iot.hc_datalinkage.service;

import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 数据联动（运输信息）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
public interface IHcDatalinkageService extends IService<HcDatalinkage> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(HcDatalinkage hcDatalinkage,List<HcDatalinkagePave> hcDatalinkagePaveList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(HcDatalinkage hcDatalinkage,List<HcDatalinkagePave> hcDatalinkagePaveList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	List<HcDatalinkage> getList(String shebeilist, Integer curid);

	List<HcDatalinkage> getListjt(String shebeilist, Integer curid);
}
