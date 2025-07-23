package com.trtm.iot.trzhanglastatistics.service;

import com.trtm.iot.trzhanglastatistics.entity.TrZhanglaStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: 张拉统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
public interface ITrZhanglaStatisticsService extends IService<TrZhanglaStatistics> {
    /**
     * 查询统计表中是否有该条数据
     */
    TrZhanglaStatistics selectlimit(Date datanyr1,String gcmc, String yzlc, String gjbh, String shebeibianhao, String gjmc, String snsjqd, String infoSbjno);

    // 统计表中添加数据
    void insert(TrZhanglaStatistics statistics);


}
