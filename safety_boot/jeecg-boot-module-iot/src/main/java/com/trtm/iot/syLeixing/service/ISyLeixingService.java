package com.trtm.iot.syLeixing.service;

import com.trtm.iot.syLeixing.entity.SyLeixing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.syLeixing.entity.SyLeixingTreeModel;

import java.util.List;

/**
 * @Description: 试验类型
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
public interface ISyLeixingService extends IService<SyLeixing> {
    SyLeixing modifyStorageObject(SyLeixing syLeixing);
}
