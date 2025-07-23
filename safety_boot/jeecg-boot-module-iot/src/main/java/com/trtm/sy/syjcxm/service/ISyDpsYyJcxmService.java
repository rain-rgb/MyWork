package com.trtm.sy.syjcxm.service;

import com.trtm.sy.syjcxm.entity.SyDpsYyJcxm;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;

/**
 * @Description: sy_dps_yy_jcxm
 * @Author: jeecg-boot
 * @Date:   2023-06-28
 * @Version: V1.0
 */
public interface ISyDpsYyJcxmService extends IService<SyDpsYyJcxm> {

    Result<?> getJcxm();

}
