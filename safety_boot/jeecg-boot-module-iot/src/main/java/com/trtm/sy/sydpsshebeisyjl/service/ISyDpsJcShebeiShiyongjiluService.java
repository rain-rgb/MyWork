package com.trtm.sy.sydpsshebeisyjl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sydpsshebeisyjl.entity.SheBeiSyJlResponse;
import com.trtm.sy.sydpsshebeisyjl.entity.SyDpsJcShebeiShiyongjilu;

/**
 * @Description: dps_jc_shebei_shiyongjilu
 * @Author: jeecg-boot
 * @Date: 2023-02-27
 * @Version: V1.0
 */
public interface ISyDpsJcShebeiShiyongjiluService extends IService<SyDpsJcShebeiShiyongjilu> {

    IPage<SheBeiSyJlResponse> getSbSyJl(String sheBeiIds, String startTime, String endTime, Integer pageNo, Integer pageSize);

}
