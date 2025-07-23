package com.trtm.iot.anquanfxgk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkWentiku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: anquan_fxfjgk_wentiku
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */

public interface AnquanFxfjgkWentikuMapper extends BaseMapper<AnquanFxfjgkWentiku> {
    List<String> getWentiNeiRong(@Param("str")String str);

}
