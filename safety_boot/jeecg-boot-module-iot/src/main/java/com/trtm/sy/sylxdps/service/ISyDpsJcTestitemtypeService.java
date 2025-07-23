package com.trtm.sy.sylxdps.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;

import java.util.List;

/**
 * @Description: sy_dps_jc_testitemtype
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface ISyDpsJcTestitemtypeService extends IService<SyDpsJcTestitemtype> {

    List<SyDpsJcTestitemtype> queryItemType(String type, Boolean isAll);

}
