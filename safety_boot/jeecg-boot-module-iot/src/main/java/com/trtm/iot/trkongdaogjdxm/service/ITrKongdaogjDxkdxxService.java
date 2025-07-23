package com.trtm.iot.trkongdaogjdxm.service;

import com.trtm.iot.trkongdaogjdxm.entity.TrKongdaogjDxkdxx;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 孔道灌浆（定性检测）孔道信息表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
public interface ITrKongdaogjDxkdxxService extends IService<TrKongdaogjDxkdxx> {

	public List<TrKongdaogjDxkdxx> selectByMainId(String mainId);
}
