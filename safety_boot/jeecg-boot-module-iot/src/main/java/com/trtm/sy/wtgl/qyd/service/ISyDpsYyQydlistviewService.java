package com.trtm.sy.wtgl.qyd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.wtgl.qyd.entity.SyDpsYyQydlistview;
import com.trtm.sy.wtgl.qyd.entity.response.YpdEntity;

import java.util.Map;

/**
 * @Description: sy_dps_yy_qydlistview
 * @Author: jeecg-boot
 * @Date:   2023-08-25
 * @Version: V1.0
 */
public interface ISyDpsYyQydlistviewService extends IService<SyDpsYyQydlistview> {

    YpdEntity queryById(String id);
}
