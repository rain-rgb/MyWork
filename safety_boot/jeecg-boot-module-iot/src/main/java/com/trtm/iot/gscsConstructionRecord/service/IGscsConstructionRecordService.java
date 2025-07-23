package com.trtm.iot.gscsConstructionRecord.service;

import com.trtm.iot.gscsConstructionRecord.entity.ConstructionStatusVo;
import com.trtm.iot.gscsConstructionRecord.entity.GscsConstructionRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 班组安全管控系统未施工记录表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
public interface IGscsConstructionRecordService extends IService<GscsConstructionRecord> {
    ConstructionStatusVo getConstructionStatusSta(String section);

    Boolean getConstructionStatus(String deptId);

    List<ConstructionStatusVo> getHistoryConstructionStatusSta(String section);
}
