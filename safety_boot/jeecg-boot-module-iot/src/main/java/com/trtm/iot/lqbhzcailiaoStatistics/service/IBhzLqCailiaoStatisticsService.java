package com.trtm.iot.lqbhzcailiaoStatistics.service;

import com.trtm.iot.lqbhzcailiaoStatistics.entity.BhzLqCailiaoStatistics;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: bhz_lq_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzLqCailiaoStatisticsService extends IService<BhzLqCailiaoStatistics> {
    BhzLqCailiaoStatistics selectId(int cs_id , String cailiaoid);


//    void updateRealityById(float reality_number , int id);
    void AddPrimary_numberOne(int id);

    void AddMiddle_numberOne(int id);

    void AddAdvanced_numberOne(int id);
}
