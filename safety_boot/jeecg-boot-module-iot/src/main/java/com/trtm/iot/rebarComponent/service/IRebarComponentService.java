package com.trtm.iot.rebarComponent.service;

import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.rebarComponent.entity.ComponentVo;

import java.util.List;

/**
 * @Description: rebar_component
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface IRebarComponentService extends IService<RebarComponent> {

    List<ComponentVo> queryComponentByTaskId(String taskId);

    boolean deletedById(String id);

    boolean addRebarComponent(RebarComponent rebarComponent);

    List<RebarComponent> queryComponentPageList(String sys_depart_orgcode, String componentName,String startDate,String endDate,String sys_depart_orgcodes,Integer status);

    RebarComponent getBycomponentId(String componentId,String orgCodes);

    List<ComponentVo> getComponentList(ComponentVo componentVo, Integer pageNo, Integer pageSize, String sys_depart_orgcode,String sys_depart_orgcodes);

    Integer editDepartComponent(List<String> ids, String orgCodes);

    boolean addTaskCheck(ComponentVo componentVo);

    boolean editComponentByTaskIdAndComponentId(String taskId, String componentId);
}
