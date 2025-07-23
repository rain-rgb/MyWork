package com.trtm.iot.pipepileYujing.service;

import com.trtm.iot.pipepileYujing.entity.PipepileYujing;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 管桩预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-09-15
 * @Version: V1.0
 */
public interface IPipepileYujingService extends IService<PipepileYujing> {

    PipepileYujing selectones( String shebeino);

}
