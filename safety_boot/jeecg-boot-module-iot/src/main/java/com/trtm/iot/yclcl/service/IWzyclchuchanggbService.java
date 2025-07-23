package com.trtm.iot.yclcl.service;

import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: wzyclchuchanggb
 * @Author: jeecg-boot
 * @Date:   2021-05-26
 * @Version: V1.0
 */
public interface IWzyclchuchanggbService extends IService<Wzyclchuchanggb> {

    List<Wzyclchuchanggb> selecones(int istongji, Integer curid);

    Boolean saveListWzycljinchanggb(List<Wzyclchuchanggb> list);

    List<Wzyclchuchanggb> slistrqid(String shebeilist, Integer curid);
}
