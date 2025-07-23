package com.trtm.iot.lqbhzStatistics.service;

import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @Description: bhz_lq_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzLqStatisticsService extends IService<BhzLqStatistics> {
        BhzLqStatistics selectlimit(Date date);

        BhzLqStatistics selectlimitone(String shebeibianhao,String formulaNo,Date date,String strengthRank,String projectName,String jobLocation,String poureLocation);

        /**
         * 查询指定的数据
         * @return
         */
        List<BhzLqStatistics> seleceZong();


    BhzLqStatistics selectbyStatistic(Date format, String shebeibianhao, String projectName, String jobLocation, String formulaNo, String strengthRank, String poureLocation);
}
