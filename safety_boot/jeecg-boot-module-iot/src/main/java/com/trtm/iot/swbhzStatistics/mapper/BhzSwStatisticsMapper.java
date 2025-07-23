package com.trtm.iot.swbhzStatistics.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import org.apache.ibatis.annotations.Mapper;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: bhz_sw_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Mapper
@Repository
public interface BhzSwStatisticsMapper extends BaseMapper<BhzSwStatistics> {
    List<Map<String,Object>> getrichaxun(String[] shebeis);
    List<Map<String,Object>> getzhouchaxun(String[] shebeis);
    List<Map<String,Object>> getyuechaxun(String[] shebeis);

    BhzSwStatistics selectbyStatistic(Date format, String shebeibianhao, String projectName, String jobLocation, String formulaNo, String strengthRank, String poureLocation);

    BhzSwStatistics selectsum(List<String> querySheBeiList, String dateNowStr);
}
