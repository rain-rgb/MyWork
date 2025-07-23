package com.trtm.iot.hc_tfysworkarea.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 土方工作区施工成果
 * @Author: jeecg-boot
 * @Date:   2023-10-10
 * @Version: V1.0
 */
public interface IHcTfysworkareaService extends IService<HcTfysworkarea> {

    String getSectionName(String sectionid);

    List<HcTfysworkarea> listbytj();

    List<HcTfysworkarea> listbytjs(String s1, String s16);
}
