package com.trtm.iot.guizhouttuisong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.guizhouttuisong.entity.GuizhoutTuisong;
import com.trtm.iot.guizhouttuisong.mapper.GuizhoutTuisongMapper;
import com.trtm.iot.guizhouttuisong.service.IGuizhoutTuisongService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: guizhout_tuisong
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Service
public class GuizhoutTuisongServiceImpl extends ServiceImpl<GuizhoutTuisongMapper, GuizhoutTuisong> implements IGuizhoutTuisongService {

    @Override
    public GuizhoutTuisong queryselectOne(String shebeino) {
        LambdaQueryWrapper<GuizhoutTuisong> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GuizhoutTuisong::getShebeiNo,shebeino);
        return this.getOne(lambdaQueryWrapper);
    }
}
