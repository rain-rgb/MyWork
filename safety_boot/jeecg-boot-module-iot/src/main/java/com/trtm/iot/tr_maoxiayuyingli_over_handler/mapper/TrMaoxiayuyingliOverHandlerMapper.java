package com.trtm.iot.tr_maoxiayuyingli_over_handler.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.tr_maoxiayuyingli_over_handler.entity.TrMaoxiayuyingliOverHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 锚下预应力张拉处置
 * @Author: jeecg-boot
 * @Date:   2024-06-06
 * @Version: V1.0
 */
public interface TrMaoxiayuyingliOverHandlerMapper extends BaseMapper<TrMaoxiayuyingliOverHandler> {

    String getDataById(String uuid);

    int chuZhiAddById(String wtyy, String clfs, String cljg, String uuid, String bizPath, String chuzhiren);

    int chuZhiUpdateById(String wtyy, String clfs, String cljg, String uuid, String bizPath, String chuzhiren);
}
