package com.trtm.iot.rebarComponent.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: rebar_component
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface RebarComponentMapper extends BaseMapper<RebarComponent> {

    List<RebarComponent> queryComponentByTaskId(@Param("taskId") String taskId,@Param("orgCode") String orgCode,@Param("orgCodes") String orgCodes);

    boolean isDeleteById(@Param("id") String id);

    List<RebarComponent> queryComponentByComponentId(@Param("componentId") String componentId);

    List<RebarComponent> queryComponentPageList(@Param("sys_depart_orgcode") String sys_depart_orgcode,
                                                @Param("componentName")  String componentName,
                                                @Param("startDate")  String startDate,
                                                @Param("endDate")  String  endDate,
                                                @Param("sys_depart_orgcodes") String sys_depart_orgcodes,
                                                @Param("status") Integer status);

    RebarComponent getBycomponentId(@Param("componentId") String componentId,@Param("orgCodes") String orgCodes);

    Integer editDepartComponent(@Param("ids") List<String> ids,@Param("orgCodes") String orgCodes);

    RebarComponent queryComponentByComponentIdAndOrgCodes(@Param("componentId") String componentId,@Param("orgCodes") String orgCodes1);

    void updateByComponentId(@Param("componentId") String componentId,@Param("componentNumber")Integer componentNumber,@Param("orgCodes")String orgCodes);

    List<RebarComponent> queryComponentPageList1(@Param("sys_depart_orgcode") String sys_depart_orgcode,
                                                 @Param("componentName")  String componentName,
                                                 @Param("startDate")  String startDate,
                                                 @Param("endDate")  String  endDate,
                                                 @Param("sys_depart_orgcodes") String sys_depart_orgcodes,
                                                 @Param("status") Integer status);

    boolean editComponentByTaskIdAndComponentId(@Param("taskId") String taskId,@Param("componentId") String componentId);
}
