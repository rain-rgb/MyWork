package com.trtm.sy.registertable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyFormReport;


public interface SyFormReportService extends IService<SyFormReport> {

    /**
     * 根据检测报告的受控编号获取完整的表单结构
     */
    SyForm getReportFormByBh(String bh);

}
