package com.trtm.iot.yajiangss.service;

import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.yajiangss.vo.TrYaJiangSSList;

import java.util.List;

/**
 * @Description: 压浆过程表
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
public interface ITrYajiangSSService extends IService<TrYajiangSS> {

    List<TrYajiangSS> selectList(String holeid);

    void saveMain(List<TrYajiangSS> trYajiangSS);

    TrYajiangSS selelctone(String sid);
}
