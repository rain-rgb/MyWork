package com.trtm.iot.hc_tfysworkarea.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.trtm.iot.hc_tfysworkarea.mapper.HcTfysworkareaMapper;
import com.trtm.iot.hc_tfysworkarea.service.IHcTfysworkareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 土方工作区施工成果
 * @Author: jeecg-boot
 * @Date:   2023-10-10
 * @Version: V1.0
 */
@Service
public class HcTfysworkareaServiceImpl extends ServiceImpl<HcTfysworkareaMapper, HcTfysworkarea> implements IHcTfysworkareaService {
    @Autowired
    private HcTfysworkareaMapper hcTfysworkareaMapper;

    @Override
    public String getSectionName(String sectionid) {
        return hcTfysworkareaMapper.getSectionName(sectionid);
    }

    @Override
    public List<HcTfysworkarea> listbytj() {
        return hcTfysworkareaMapper.listbytj();
    }

    @Override
    public List<HcTfysworkarea> listbytjs(String s1, String s16) {
        return hcTfysworkareaMapper.listbytjs(s1,s16);
    }

}
