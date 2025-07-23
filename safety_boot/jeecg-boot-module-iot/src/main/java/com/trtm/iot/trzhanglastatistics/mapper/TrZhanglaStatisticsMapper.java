package com.trtm.iot.trzhanglastatistics.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.trzhanglastatistics.entity.TrZhanglaStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 张拉统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
public interface TrZhanglaStatisticsMapper extends BaseMapper<TrZhanglaStatistics> {

    TrZhanglaStatistics selectlimit( Date datanyr1, String gcmc, String yzlc, String gjbh, String shebeibianhao, String gjmc, String snsjqd,String infoSbjno);
}
