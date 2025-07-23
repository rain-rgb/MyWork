package com.trtm.sy.sylxdps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sylxdps.entity.SyDpsJcTestitem;

import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_jc_testitem
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface ISyDpsJcTestitemService extends IService<SyDpsJcTestitem> {

    List<Map> getList(String titCode);

    Map findById(String id);

    Map findSamp(String id);

    List<Map<String, Object>> typeList(String type, String bg, Boolean isAll);
}
