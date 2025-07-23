package com.trtm.iot.tryajiangstatistics.mapper;

import java.util.Date;
import java.util.List;

import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.tryajiangstatistics.entity.TrYajiangStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 压浆统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-10
 * @Version: V1.0
 */
public interface TrYajiangStatisticsMapper extends BaseMapper<TrYajiangStatistics> {

    TrYajiangStatistics selectlimit(Date datanyr1, String htbh, String gcmc, String zhbw, String sgbw, String gjjg, String gjbh, String yjsbbh, String lblx, String infoSbjno);

}
