package com.trtm.iot.entranceGuardRecord.service.impl;

import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.mapper.EntranceGuardRecordMapper;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 门禁考勤表信息
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
@Service
public class EntranceGuardRecordServiceImpl extends ServiceImpl<EntranceGuardRecordMapper, EntranceGuardRecord> implements IEntranceGuardRecordService {
    @Autowired
    private EntranceGuardRecordMapper entranceGuardRecordMapper;
    @Override
    public List<EntranceGuardRecord> selectLists(String shebeiNo, Integer id) {
        return entranceGuardRecordMapper.selectLists(shebeiNo,id);
    }
}
