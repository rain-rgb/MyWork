package com.trtm.iot.hc_transportrecords.service;

import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 运输数据子表
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
public interface IHcTransportrecordsPaveService extends IService<HcTransportrecordsPave> {

	public List<HcTransportrecordsPave> selectByMainId(String mainId);
}
