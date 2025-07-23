package com.trtm.iot.lqbhzStatistics.mapper;

import java.util.Date;
import java.util.List;

import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bhz_lq_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface BhzLqStatisticsMapper extends BaseMapper<BhzLqStatistics> {

    /**
     * 沥青统计查询
     * @param shebeibianhao
     * @param formulaNo
     * @param statistics_time
     * @return
     */
    BhzLqStatistics selectlimitone(String shebeibianhao, String formulaNo, Date statistics_time,String strengthRank,String projectName,String jobLocation,String poureLocation);

    BhzLqStatistics selectbyStatistic(Date format, String shebeibianhao, String projectName, String jobLocation, String formulaNo, String strengthRank, String poureLocation);

    BhzLqStatistics selectsum(List<String> querySheBeiList, String dateNowStr);
}
