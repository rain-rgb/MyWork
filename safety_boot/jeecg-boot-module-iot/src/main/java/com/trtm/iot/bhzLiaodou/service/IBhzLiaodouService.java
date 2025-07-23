package com.trtm.iot.bhzLiaodou.service;

import com.trtm.iot.bhzLiaodou.entity.BhzLiaodou;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bhz_liaodou
 * @Author: jeecg-boot
 * @Date:   2023-01-29
 * @Version: V1.0
 */
public interface IBhzLiaodouService extends IService<BhzLiaodou> {

    BhzLiaodou selectByMaterialNo(String materialNo, String sysOrgCode);
}
