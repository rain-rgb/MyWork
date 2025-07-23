package com.trtm.iot.bhzlqjipeistatistics.service;

import com.trtm.iot.bhzlqjipeistatistics.entity.BhzLqJipeiStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 沥青级配统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
public interface IBhzLqJipeiStatisticsService extends IService<BhzLqJipeiStatistics> {

    BhzLqJipeiStatistics selectone(String baseGuid, String shebeibianhao, String shaikong);

    List<BhzLqJipeiStatistics> selectList(String shebeino ,Integer id);

    List<BhzLqJipeiStatistics> getList1(String baseid);
}
