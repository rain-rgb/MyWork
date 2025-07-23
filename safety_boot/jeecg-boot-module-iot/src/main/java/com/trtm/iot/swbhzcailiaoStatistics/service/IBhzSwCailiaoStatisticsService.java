package com.trtm.iot.swbhzcailiaoStatistics.service;

import com.trtm.iot.swbhzcailiaoStatistics.entity.BhzSwCailiaoStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bhz_sw_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzSwCailiaoStatisticsService extends IService<BhzSwCailiaoStatistics> {

    BhzSwCailiaoStatistics selectone(int cs_id, String cailiaoid);

    int AddReality_numberOne(Integer id,Float reality_number);

    int AddTheory_numberOne(Integer id,Float theory_number);

    int AddPrimary_numberOne(Integer id);

    int AddMiddle_numberOne(Integer id);

    int AddAdvanced_numberOne(Integer id);
}
