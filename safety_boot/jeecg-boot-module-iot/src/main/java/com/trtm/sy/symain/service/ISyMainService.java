package com.trtm.sy.symain.service;

import com.trtm.sy.symain.entity.SyMain;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.symainprocess.entity.SyMainProcess;

import java.util.List;

/**
 * @Description: sy_main
 * @Author: jeecg-boot
 * @Date:   2022-09-08
 * @Version: V1.0
 */
public interface ISyMainService extends IService<SyMain> {
     List<SyMainProcess> getDetails(String uuid);
}
