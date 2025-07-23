package com.trtm.iot.entranceGuardRecord.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 门禁考勤表信息
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
public interface EntranceGuardRecordMapper extends BaseMapper<EntranceGuardRecord> {

    List<EntranceGuardRecord> selectLists(String shebeiNo, Integer id);
}
