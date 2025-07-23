package com.trtm.iot.zhangla.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.zhangla.entity.TrZhangla;

import java.util.List;
import java.util.Map;

/**
 * @Description: tr_zhangla
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
public interface ITrZhanglaService extends IService<TrZhangla> {
    Map queryDetails(Integer type,String shebeiNo,Integer pageNo,Integer pageSize);

    Map queryDe(String syjid, Integer type);
}
