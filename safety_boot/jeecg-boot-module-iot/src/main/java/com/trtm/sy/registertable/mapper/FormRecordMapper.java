package com.trtm.sy.registertable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.registertable.model.SyFormRecord;

public interface FormRecordMapper extends BaseMapper<SyFormRecord> {

    void delByTableNum(String tableNum);
}
