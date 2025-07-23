package com.trtm.iot.bhzSwJipeiStatistics.service;

import com.trtm.iot.bhzSwJipeiStatistics.entity.BhzSwJipeiStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: bhz_sw_jipei_statistics
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
public interface IBhzSwJipeiStatisticsService extends IService<BhzSwJipeiStatistics> {

    BhzSwJipeiStatistics selectone(String baseGuid, String shebeibianhao, String shaikong);

    List<BhzSwJipeiStatistics> selectList1(String shebeilist, Integer curid);

    List<BhzSwJipeiStatistics> getList1(String baseid1);
}
