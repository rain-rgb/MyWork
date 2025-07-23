package com.trtm.iot.gscsConstructionRecord.mapper;

import java.util.List;

import com.trtm.iot.gscsConstructionRecord.entity.ConstructionStatusVo;
import com.trtm.iot.gscsConstructionRecord.entity.GscsConstructionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 班组安全管控系统未施工记录表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
public interface GscsConstructionRecordMapper extends BaseMapper<GscsConstructionRecord> {
    List<ConstructionStatusVo> getHistoryConstructionStatusSta();
}
