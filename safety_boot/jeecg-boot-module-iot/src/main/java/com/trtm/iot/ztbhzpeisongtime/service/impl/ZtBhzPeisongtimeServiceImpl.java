package com.trtm.iot.ztbhzpeisongtime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.qrcode.entity.Qrcode;
import com.trtm.iot.ztbhzpeisongtime.entity.ZtBhzPeisongtime;
import com.trtm.iot.ztbhzpeisongtime.mapper.ZtBhzPeisongtimeMapper;
import com.trtm.iot.ztbhzpeisongtime.service.IZtBhzPeisongtimeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 自建拌和站配送考核标准时间表
 * @Author: jeecg-boot
 * @Date:   2023-11-01
 * @Version: V1.0
 */
@Service
public class ZtBhzPeisongtimeServiceImpl extends ServiceImpl<ZtBhzPeisongtimeMapper, ZtBhzPeisongtime> implements IZtBhzPeisongtimeService {
    ZtBhzPeisongtimeMapper ztBhzPeisongtimeMapper;

    @Override
    public ZtBhzPeisongtime selecdw(String projname) {
        return ztBhzPeisongtimeMapper.selecdw(projname);
    }

    @Override
    public ZtBhzPeisongtime ztyjsb(String projname) {
        return ztBhzPeisongtimeMapper.ztyjsb(projname);
    }
}
