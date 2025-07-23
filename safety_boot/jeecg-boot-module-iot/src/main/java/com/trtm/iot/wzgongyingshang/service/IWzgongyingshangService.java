package com.trtm.iot.wzgongyingshang.service;

import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 物资供应商主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
public interface IWzgongyingshangService extends IService<Wzgongyingshang> {

    Map selectqueryone(String sysOrgCode);

    Wzgongyingshang selectnameone(String guid);
    List<Wzgongyingshang> gyslist(String departid);

    List<Map> queryList(String sysOrgCode);
}
