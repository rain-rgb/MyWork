package com.trtm.iot.trzhanglarenwudan.service;

import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;

import java.util.List;
import java.util.Map;

/**
 * @Description: 张拉任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface ITrZhanglaRenwudanService extends IService<TrZhanglaRenwudan> {

    TrZhanglaRenwudan selectone(String uuid);

    List<TrZhanglaRenwudan> selectLists(String strsToList1, Integer curid);

    List<String> saveShebei(List<String> sbs);

    List<TrZhanglaRenwudan> saveShebeis(List<String> sbs);

    Map selectwbs(String sgbwuuid);
}
