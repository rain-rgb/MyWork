package com.trtm.iot.rebarComponent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.trtm.iot.rebarComponent.vo.RebarComponentTaskVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangqi
 * @date 2023/6/16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ApiModel(value="rebar_component对象", description="rebar_component")
public class ComponentVo implements Serializable {
    /**构件集合*/
    @Excel(name = "构件集合", width = 15)
    @ApiModelProperty(value = "构件集合")
    private List<RebarComponent> rebarComponents;
    /**任务编号*/
//    @Excel(name = "任务编号", width = 15)
    @ApiModelProperty(value = "任务编号")
    private java.lang.String taskId;
    /**构件编号*/
    @Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "构件编号")
    private java.lang.String componentId;
    /**构件名称*/
    @Excel(name = "构件名称", width = 15)
    @ApiModelProperty(value = "构件名称")
    private java.lang.String componentName;
    /**构件规格型号*/
    @Excel(name = "构件规格型号", width = 15)
    @ApiModelProperty(value = "构件规格型号")
    private java.lang.String componentModel;

    /**重量*/
    @Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private java.lang.Double weight;
    /**总重量*/
    @Excel(name = "总重量", width = 15)
    @ApiModelProperty(value = "总重量")
    private java.lang.Double totalWeight;
    /**原料消耗总重量*/
    @Excel(name = "原料消耗总重量", width = 15)
    @ApiModelProperty(value = "原料消耗总重量")
    private java.lang.Double materialTotalWeight;
    /**构件数量*/
    @Excel(name = "构件数量", width = 15)
    @ApiModelProperty(value = "构件数量")
    private String componentNumber;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    private static final long serialVersionUID = 1L;

    /**主键id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**单位*/
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    @Dict(dicCode = "WZCaiLiaoJiLiangDanWei")
    private java.lang.String unit;

    /**组织机构编码*/
    @Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCode;
    /**分部分项组织机构编码*/
    @Excel(name = "分部分项组织机构编码", width = 15)
    @ApiModelProperty(value = "分部分项组织机构编码")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCodes;
    /**构件类型*/
    @Excel(name = "构件类型", width = 15)
    @ApiModelProperty(value = "构件类型")
    private java.lang.String componentType;

    /**逻辑删除标识*/
    @Excel(name = "逻辑删除标识", width = 15)
    @ApiModelProperty(value = "逻辑删除标识")
    private java.lang.Integer isDeleted;
    /**创建人*/
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createPerson;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    @ApiModelProperty(value = "开始时间", required = true, dataType = "String")
    private String startDate;

    @ApiModelProperty(value = "结束时间", required = true, dataType = "String")
    private String endDate;
    /**修改人*/
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updatePerson;
    /**修改时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
}
