package com.trtm.iot.bhzlqjipeifanwei.service;

import com.trtm.iot.bhzlqjipeifanwei.entity.BhzLqJipeiFanwei;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 沥青级配范围配置表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
public interface IBhzLqJipeiFanweiService extends IService<BhzLqJipeiFanwei> {

    BhzLqJipeiFanwei selectone(String jipeibiaozhun, String formulaNo);

    List<BhzLqJipeiFanwei> selectlist(String shebeino, Integer id);
}
