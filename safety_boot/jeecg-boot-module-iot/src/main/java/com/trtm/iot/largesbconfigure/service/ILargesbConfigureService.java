package com.trtm.iot.largesbconfigure.service;

import com.trtm.iot.largesbconfigure.entity.LargesbConfigure;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: largesb_configure
 * @Author: jeecg-boot
 * @Date:   2023-09-12
 * @Version: V1.0
 */
public interface ILargesbConfigureService extends IService<LargesbConfigure> {

    void saveDoorSbj(String sysOrgCode, String shebeiNo, String doorSbjno);
}
