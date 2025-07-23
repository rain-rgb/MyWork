package com.trtm.iot.TbhzcailiaoStatistics.service;

import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: bhz_cement_detail_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzCementDetailStatisticsService extends IService<BhzCementDetailStatistics> {

    BhzCementDetailStatistics selectone(int cs_id, String cailiaoid);

    int AddReality_numberOne(Integer id,Float reality_number);

    int AddTheory_numberOne(Integer id,Float theory_number);

    int AddPrimary_numberOne(Integer id);

    int AddMiddle_numberOne(Integer id);

    int AddAdvanced_numberOne(Integer id);

    /**
     * 根据唯一id查询砼拌合站统计的材料
     * @param id
     * @return
     */
    List<BhzCementDetailStatistics> selectdetaillist(Integer id);

    List<BhzCementDetailStatistics> selectShebeiDetail(String statisticsTime_begin, String statisticsTime_end, List<String> shebeiNo);
}
