package com.trtm.sy.registertable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyFormRecord;



public interface SyFormRecordService extends IService<SyFormRecord> {

    SyForm getRecordFormByBh(String bh, String sampleId, String type);


}
