package com.trtm.iot.trliangchanginfo.service;

import com.trtm.iot.trliangchanginfo.entity.TrLiangchangInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: tr_liangchang_info
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface ITrLiangchangInfoService extends IService<TrLiangchangInfo> {


    List<Map<String, Object>> selectbyorgcode(String dateNowStr,String orgCode);

    List<Map<String, Object>> seyorgcodexq(String orgCode, Integer type);
}
