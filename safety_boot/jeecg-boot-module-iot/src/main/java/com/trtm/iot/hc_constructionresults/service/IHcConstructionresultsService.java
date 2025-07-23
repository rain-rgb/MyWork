package com.trtm.iot.hc_constructionresults.service;

import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 施工成果
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface IHcConstructionresultsService extends IService<HcConstructionresults> {

    String selectbypjidmid(String pjid, String machineid);

    List<String> selecid(String date_begin, String date_end, List<String> l);

    void updateSection(String ids, String projectid, String sectionid);
}
