package com.trtm.iot.tsyjzbStatistics.service;

import com.trtm.iot.tsyjzbStatistics.entity.TSyjzbStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: t_syjzb_statistics
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
public interface ITSyjzbStatisticsService extends IService<TSyjzbStatistics> {

    TSyjzbStatistics selectLimit(String sbbh, String sylx, String wtbh, String sjqd, Date stringnyr);

    boolean updatestatisticsone(int id, Integer allsums, Integer allOversums);

}
