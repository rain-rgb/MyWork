package com.trtm.sy.sylxdps.mapper;

import java.util.List;

import com.trtm.sy.sylxdps.entity.SyDpsSyReportM;
import org.apache.ibatis.annotations.Param;
import com.trtm.sy.sylxdps.entity.SyDpsSyReportS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: sy_dps_sy_report_s
 * @Author: jeecg-boot
 * @Date:   2023-02-10
 * @Version: V1.0
 */
public interface SyDpsSyReportSMapper extends BaseMapper<SyDpsSyReportS> {

    SyDpsSyReportM selectBySampleno(String sampleno);
}
