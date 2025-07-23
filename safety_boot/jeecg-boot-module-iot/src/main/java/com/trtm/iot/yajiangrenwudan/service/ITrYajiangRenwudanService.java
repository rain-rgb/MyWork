package com.trtm.iot.yajiangrenwudan.service;

import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 压浆任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-08
 * @Version: V1.0
 */
public interface ITrYajiangRenwudanService extends IService<TrYajiangRenwudan> {
    TrYajiangRenwudan selectone(String uuid);

    List<TrYajiangRenwudan> selectLists(String strsToList1, Integer curid);

    List<String> saveShebei(List<String> sbs);

    List<TrYajiangRenwudan> saveShebeis(List<String> sbs);
}
