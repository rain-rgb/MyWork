package com.trtm.iot.lowstrain_m.service;

import com.trtm.iot.lowstrain_m.entity.LowstrainS;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 低应变子表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
public interface ILowstrainSService extends IService<LowstrainS> {

	public List<LowstrainS> selectByMainId(String mainId);
}
