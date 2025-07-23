package com.trtm.iot.bhzyclcalculate.service;

import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.bhzyclcalculate.entity.BhzYclcalculate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: bhz_yclcalculate
 * @Author: jeecg-boot
 * @Date:   2023-10-24
 * @Version: V1.0
 */
public interface IBhzYclcalculateService extends IService<BhzYclcalculate> {

    List<Map<String,Object>> getTJInfoBySbjno(String shebeiNo, String statisticsTime_begin, String statisticsTime_end);
}
