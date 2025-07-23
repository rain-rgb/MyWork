package com.trtm.sy.tableform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.trtm.sy.tableform.model.TableForm;

import java.util.List;

public interface TableFormService extends IService<TableForm> {



    List<SyDpsJcTestitemtype> getExperimentType(String roleId);

    List<TableForm> getExperimentChridList(String roleId, String titcode);
}
