package com.trtm.iot.tryajiangstatistics.service;

import com.trtm.iot.tryajiangstatistics.entity.TrYajiangStatistics;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;

import java.util.Date;

/**
 * @Description: 压浆统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-10
 * @Version: V1.0
 */
public interface ITrYajiangStatisticsService extends IService<TrYajiangStatistics> {

    TrYajiangStatistics selectlimit(Date datanyr1, String htbh, String gcmc, String zhbw, String sgbw, String gjjg, String gjbh, String yjsbbh, String lblx, String infoSbjno);

    void insert(TrYajiangStatistics statistics);
}
