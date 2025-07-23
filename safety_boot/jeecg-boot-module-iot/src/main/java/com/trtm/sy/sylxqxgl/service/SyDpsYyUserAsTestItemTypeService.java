package com.trtm.sy.sylxqxgl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sylxqxgl.entity.SyDpsYyUserAsTestItemType;
import com.trtm.sy.sylxqxgl.entity.SyJlbResponse;
import com.trtm.sy.sylxqxgl.entity.SyLxResponse;

import java.util.List;

public interface SyDpsYyUserAsTestItemTypeService extends IService<SyDpsYyUserAsTestItemType> {

    void saveRoleSyLx(List<SyDpsYyUserAsTestItemType> entity);

    List<SyLxResponse> getSyLxData();

    List<String> getSyLxByRoleId(String roleId);

    List<SyJlbResponse> getSyLxDataByTitCode(String titCode);

    List<String> getSyLxJlbByRoleId(String roleId, String titCode);

    void saveSyLx(SyDpsYyUserAsTestItemType entity);

    void saveParameter(SyDpsYyUserAsTestItemType entity);
}
