package com.trtm.iot.anquanfxgk.service.impl;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkWentiku;
import com.trtm.iot.anquanfxgk.mapper.AnquanFxfjgkWentikuMapper;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkWentikuService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: anquan_fxfjgk_wentiku
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
@Service
public class AnquanFxfjgkWentikuServiceImpl extends ServiceImpl<AnquanFxfjgkWentikuMapper, AnquanFxfjgkWentiku> implements IAnquanFxfjgkWentikuService {
    @Autowired
    private AnquanFxfjgkWentikuMapper mapper;


    @Override
    public List<String> getWentiNeiRong(String str) {
        return mapper.getWentiNeiRong(str);
    }
}
