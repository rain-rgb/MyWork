package com.trtm.iot.hc_datalinkage.service;

import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 数据联动（摊铺数据）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
public interface IHcDatalinkagePaveService extends IService<HcDatalinkagePave> {

	public List<HcDatalinkagePave> selectByMainId(String mainId);
}
