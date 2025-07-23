package com.trtm.iot.rebarComponent.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author zhangqi
 * @date 2023/6/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebarComponentTaskVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
    /**构件主键id*/
    @Excel(name = "构件主键id", width = 15)
    @ApiModelProperty(value = "构件主键id")
    private java.lang.String componentId;
    /**任务清单主键id*/
    @Excel(name = "任务清单主键id", width = 15)
    @ApiModelProperty(value = "任务清单主键id")
    private java.lang.String taskId;
    /**设计构件数量*/
    @Excel(name = "设计构件数量", width = 15)
    @ApiModelProperty(value = "设计构件数量")
    private java.lang.String designComponentNumber;
    /**领用构件数量*/
    @Excel(name = "领用构件数量", width = 15)
    @ApiModelProperty(value = "领用构件数量")
    private java.lang.String actualComponentNumber;
    /**构件数量*/
    @Excel(name = "构件数量", width = 15)
    @ApiModelProperty(value = "构件数量")
    private java.lang.String componentNumber;
    /**构件名称*/
    @Excel(name = "构件名称", width = 15)
    @ApiModelProperty(value = "构件名称")
    private java.lang.String componentName;
    /**构件规格型号*/
    @Excel(name = "构件规格型号", width = 15)
    @ApiModelProperty(value = "构件规格型号")
    private java.lang.String componentModel;
    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**单位*/
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    @Dict(dicCode = "WZCaiLiaoJiLiangDanWei")
    private java.lang.String unit;
    /**重量*/
    @Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private java.lang.Double weight;
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
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
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
