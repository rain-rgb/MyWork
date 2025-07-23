package com.trtm.iot.guizhouttuisong.service;

import com.trtm.iot.guizhouttuisong.entity.GuizhoutTuisong;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: guizhout_tuisong
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
public interface IGuizhoutTuisongService extends IService<GuizhoutTuisong> {

    GuizhoutTuisong queryselectOne(String shebeino);

}
