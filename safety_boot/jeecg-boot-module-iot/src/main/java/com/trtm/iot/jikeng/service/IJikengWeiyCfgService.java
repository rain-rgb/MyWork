package com.trtm.iot.jikeng.service;

import com.trtm.iot.jikeng.entity.JikengWeiyCfg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: jikeng_weiy_cfg
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
public interface IJikengWeiyCfgService extends IService<JikengWeiyCfg> {

    void updateByShbeino(String shebeino,String jsondata);

}
