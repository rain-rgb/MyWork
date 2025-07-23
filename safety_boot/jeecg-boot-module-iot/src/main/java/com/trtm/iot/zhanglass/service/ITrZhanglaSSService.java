package com.trtm.iot.zhanglass.service;

import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 张拉过程子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
public interface ITrZhanglaSSService extends IService<TrZhanglaSS> {

    List<TrZhanglaSS> getselectList(String holeid);

    void saveMain(List<TrZhanglaSS> trZhanglaSSList);
}
