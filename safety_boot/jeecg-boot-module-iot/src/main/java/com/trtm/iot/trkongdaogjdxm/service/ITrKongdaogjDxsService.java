package com.trtm.iot.trkongdaogjdxm.service;

import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxs;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 孔道灌浆（定性检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface ITrKongdaogjDxsService extends IService<TrKongdaogjDxs> {

	public List<TrKongdaogjDxs> selectByMainId(String mainId);
}
