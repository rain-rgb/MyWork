package com.trtm.iot.hc_transportrecords.service;

import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 运输数据
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
public interface IHcTransportrecordsService extends IService<HcTransportrecords> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(HcTransportrecords hcTransportrecords,List<HcTransportrecordsPave> hcTransportrecordsPaveList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(HcTransportrecords hcTransportrecords,List<HcTransportrecordsPave> hcTransportrecordsPaveList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

    List<HcTransportrecords> getList(String shebeilist, Integer curid);

    List<HcTransportrecords> getListjt(String shebeilist, Integer curid);
}
