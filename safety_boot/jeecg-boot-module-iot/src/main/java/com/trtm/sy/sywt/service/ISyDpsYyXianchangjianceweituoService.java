package com.trtm.sy.sywt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.syrules.entity.SyCodingRules;
import com.trtm.sy.sywt.entity.SyDpsYyXianchangjianceweituo;
import com.trtm.sy.sywt.entity.XcWtRequest;
import com.trtm.sy.sywt.entity.XcWtResponse;

import java.text.ParseException;
import java.util.Map;

/**
 * @Description: sy_dps_yy_xianchangjianceweituo
 * @Author: jeecg-boot
 * @Date: 2023-06-28
 * @Version: V1.0
 */
public interface ISyDpsYyXianchangjianceweituoService extends IService<SyDpsYyXianchangjianceweituo> {

    void saveWeiTuoDan(SyDpsYyXianchangjianceweituo xianchangjianceweituo) throws ParseException;

    String getBH(String bhgz, SyCodingRules rules, String table, String liushuihao, String titCode) throws ParseException;

    IPage<XcWtResponse> getListXcWt(XcWtRequest request);

    Map<String, Object> getXcWtById(Integer id);
}
