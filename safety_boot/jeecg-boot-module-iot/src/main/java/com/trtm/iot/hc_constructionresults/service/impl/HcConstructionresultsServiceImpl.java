package com.trtm.iot.hc_constructionresults.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.mapper.HcConstructionresultsMapper;
import com.trtm.iot.hc_constructionresults.service.IHcConstructionresultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 施工成果
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class HcConstructionresultsServiceImpl extends ServiceImpl<HcConstructionresultsMapper, HcConstructionresults> implements IHcConstructionresultsService {
    @Autowired
    HcConstructionresultsMapper hcConstructionresultsMapper;

    @Override
    public String selectbypjidmid(String pjid, String machineid) {
        return hcConstructionresultsMapper.selectbypjidmid(pjid,machineid);
    }

    @Override
    public List<String> selecid(String date_begin, String date_end, List<String> l) {
        return hcConstructionresultsMapper.selecid(date_begin,date_end,l);
    }

    @Override
    public void updateSection(String ids, String projectid, String sectionid) {
         hcConstructionresultsMapper.updateSection( ids,  projectid,  sectionid);
    }
}
