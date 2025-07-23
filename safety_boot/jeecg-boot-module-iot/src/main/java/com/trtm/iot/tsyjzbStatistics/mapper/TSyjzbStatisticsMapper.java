package com.trtm.iot.tsyjzbStatistics.mapper;

import java.util.Date;

import com.trtm.iot.tsyjzbStatistics.entity.TSyjzbStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: t_syjzb_statistics
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
public interface TSyjzbStatisticsMapper extends BaseMapper<TSyjzbStatistics> {

    TSyjzbStatistics selectLimit(String sbbh, String sylx, String wtbh, String sjqd, Date stringnyr);

    @Update("update t_syjzb_statistics set all_dish=#{allsums}, all_overproof_dish=#{allOversums} where id = #{id}")
    boolean updatestatisticsone(int id, Integer allsums, Integer allOversums);

}
