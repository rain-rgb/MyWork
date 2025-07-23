package com.trtm.iot.byscfg.service;

import com.trtm.iot.byscfg.entity.BysCfg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 标养室温湿度配置表
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
public interface IBysCfgService extends IService<BysCfg> {

    BysCfg selectbyscallone(String shebeino);

    List<BysCfg> selectbyslist(String shebeino);
}
