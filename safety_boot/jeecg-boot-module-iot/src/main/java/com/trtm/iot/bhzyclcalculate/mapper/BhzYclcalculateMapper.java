package com.trtm.iot.bhzyclcalculate.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzyclcalculate.entity.BhzYclcalculate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bhz_yclcalculate
 * @Author: jeecg-boot
 * @Date:   2023-10-24
 * @Version: V1.0
 */
public interface BhzYclcalculateMapper extends BaseMapper<BhzYclcalculate> {

    List<Map<String, Object>> getTJInfoBySbjno(String shebeiNo, String statisticsTime_begin, String statisticsTime_end);
}
