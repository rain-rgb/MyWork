package com.trtm.iot.anquanfxgk.service;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkWentiku;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: anquan_fxfjgk_wentiku
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
public interface IAnquanFxfjgkWentikuService extends IService<AnquanFxfjgkWentiku> {
    List<String> getWentiNeiRong(@Param("str")String str);
}
