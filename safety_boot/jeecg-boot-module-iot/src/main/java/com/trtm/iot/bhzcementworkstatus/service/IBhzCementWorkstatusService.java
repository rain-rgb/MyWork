package com.trtm.iot.bhzcementworkstatus.service;

import com.trtm.iot.bhzcementworkstatus.entity.BhzCementWorkstatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 砼拌合站工作状态表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
public interface IBhzCementWorkstatusService extends IService<BhzCementWorkstatus> {

    BhzCementWorkstatus queryone(String shebeiNo, String sysOrgCode);

    int updatestatus(int id, int status);
}
