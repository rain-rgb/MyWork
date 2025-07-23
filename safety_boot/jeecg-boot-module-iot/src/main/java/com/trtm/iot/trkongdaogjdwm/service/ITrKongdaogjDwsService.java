package com.trtm.iot.trkongdaogjdwm.service;

import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDws;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 孔道灌浆（定位检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface ITrKongdaogjDwsService extends IService<TrKongdaogjDws> {

	public List<TrKongdaogjDws> selectByMainId(String mainId);
}
