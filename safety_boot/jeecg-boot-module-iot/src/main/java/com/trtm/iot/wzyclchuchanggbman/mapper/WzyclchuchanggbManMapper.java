package com.trtm.iot.wzyclchuchanggbman.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzyclchuchanggbman.entity.WzyclchuchanggbMan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: wzyclchuchanggb_man
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
public interface WzyclchuchanggbManMapper extends BaseMapper<WzyclchuchanggbMan> {

    Integer updateWzliaocangljshiyongByGuid(@Param("num") Double num,@Param("guid") String guid);

    Integer updateUseNumByPici(@Param("num") Double num,@Param("id") String pici);
}
