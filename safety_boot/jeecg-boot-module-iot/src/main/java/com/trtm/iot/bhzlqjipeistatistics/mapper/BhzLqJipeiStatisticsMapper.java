package com.trtm.iot.bhzlqjipeistatistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzlqjipeistatistics.entity.BhzLqJipeiStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 沥青级配统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
public interface BhzLqJipeiStatisticsMapper extends BaseMapper<BhzLqJipeiStatistics> {

    List<BhzLqJipeiStatistics> selectList(String shebeino, Integer id);

    List<BhzLqJipeiStatistics> getList1(String baseid);
}
