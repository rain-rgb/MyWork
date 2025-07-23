package com.trtm.iot.dianzhiweilan.service;

import com.trtm.iot.dianzhiweilan.entity.Dianzhiweilan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 新电子围栏表
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
public interface IDianzhiweilanService extends IService<Dianzhiweilan> {

    List<String> selectshebei();

    List<Map<String, Object>> selectjinduweidu(String shebei,Integer i);
}
