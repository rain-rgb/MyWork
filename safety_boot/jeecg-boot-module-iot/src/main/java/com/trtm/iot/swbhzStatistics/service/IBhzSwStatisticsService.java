package com.trtm.iot.swbhzStatistics.service;

import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: bhz_sw_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzSwStatisticsService extends IService<BhzSwStatistics> {

    BhzSwStatistics selectone(Date date, String shebeibianhao);

    BhzSwStatistics selectbyStatistic(Date format, String shebeibianhao, String projectName, String jobLocation, String formulaNo, String strengthRank, String poureLocation);
}
