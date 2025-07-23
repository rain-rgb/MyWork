package com.trtm.iot.bhzSwJipeiStatistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzSwJipeiStatistics.entity.BhzSwJipeiStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bhz_sw_jipei_statistics
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
public interface BhzSwJipeiStatisticsMapper extends BaseMapper<BhzSwJipeiStatistics> {

    List<BhzSwJipeiStatistics> selectList1(String shebeilist, Integer curid);

    List<BhzSwJipeiStatistics> getList1(String baseid1);
}
