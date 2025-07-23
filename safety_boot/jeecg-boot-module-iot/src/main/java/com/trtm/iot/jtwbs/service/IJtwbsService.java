package com.trtm.iot.jtwbs.service;

import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: jtwbs
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface IJtwbsService extends IService<Jtwbs> {

    Jtwbs selectbycogcode(String orgCode);

    List<Jtwbs> selectbycogcodelist(String orgCode);
}
