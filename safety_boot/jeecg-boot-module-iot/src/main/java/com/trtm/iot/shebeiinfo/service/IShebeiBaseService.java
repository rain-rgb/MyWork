package com.trtm.iot.shebeiinfo.service;

import com.trtm.iot.shebeiinfo.entity.ShebeiBase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: shebei_base
 * @Author: jeecg-boot
 * @Date:   2024-11-13
 * @Version: V1.0
 */
public interface IShebeiBaseService extends IService<ShebeiBase> {

    ShebeiBase getByShebeino(String shebeino );
}
