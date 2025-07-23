package com.trtm.iot.entranceGuardRecord.service;

import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 门禁考勤表信息
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
public interface IEntranceGuardRecordService extends IService<EntranceGuardRecord> {

    List<EntranceGuardRecord> selectLists(String shebeiNo, Integer id);

}
