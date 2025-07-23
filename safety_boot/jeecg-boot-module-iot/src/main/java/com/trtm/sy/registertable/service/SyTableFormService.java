package com.trtm.sy.registertable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyTableForm;
import com.trtm.sy.registertable.model.request.SyTableFormRequest;


import java.io.IOException;

public interface SyTableFormService extends IService<SyTableForm> {

    IPage<SyTableForm> queryFormList(SyTableFormRequest request);

    void saveTableData(SyForm entity);

    SyForm getTableData(String sampleId, String tableNumber);

    boolean addSyTableForm(SyTableForm request);

    boolean deleteSyTableForm(SyTableForm request);


}
