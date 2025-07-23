package com.trtm.iot.wzycljinchanggbman.service;

import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;

import java.util.List;

/**
 * @Description: wzycljinchanggb_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
public interface IWzycljinchanggbManService extends IService<WzycljinchanggbMan> {

    List<WzycljinchanggbMan> selectycljinchangList(Integer curid, int taizhangtj);

    void updateistongji(Integer id, Integer taizhangtj);

    Integer getJYStatus(Integer taizhangid);

    List<String> getCailiaoByNodetype(String nodetype);

    Boolean saveListWzycljinchanggb(List<WzycljinchanggbMan> list);
}
