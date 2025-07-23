package com.trtm.iot.rebarTaskChecklist.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: rebar_task_checklist
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Data
@TableName("rebar_task_checklist")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rebar_task_checklist对象", description="rebar_task_checklist")
public class RebarTaskChecklist implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**任务编号*/
	@Excel(name = "任务编号", width = 15)
    @ApiModelProperty(value = "任务编号")
    private java.lang.String taskId;
    /**加工厂编号*/
    @Excel(name = "加工厂编号", width = 15)
    @ApiModelProperty(value = "加工厂编号")
    private String factoryId;

	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String constructionSite;
	/**班组长*/
	@Excel(name = "班组长", width = 15)
    @ApiModelProperty(value = "班组长")
    private java.lang.String teamLeader;
    /**班组长编号*/
    @Excel(name = "班组长编号", width = 15)
    @ApiModelProperty(value = "班组长编号")
    private java.lang.String teamLeaderId;
	/**申请人*/
	@Excel(name = "申请人", width = 15)
    @ApiModelProperty(value = "申请人")
    private java.lang.String applicant;
	/**申请单位*/
	@Excel(name = "申请单位", width = 15)
    @ApiModelProperty(value = "申请单位")
    private java.lang.String applicantUnit;
    /**加工负责人*/
    @Excel(name = "加工负责人", width = 15)
    @ApiModelProperty(value = "加工负责人")
    private java.lang.String processors;
	/**组织机构编码*/
	@Excel(name = "分部分项机构编码", width = 15)
    @ApiModelProperty(value = "分部分项机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCode;
    /**分部分项机构编码*/
    @Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCodes;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "rebar_task_status")
    private java.lang.String status;
    /**验收人员*/
    @Excel(name = "验收人员", width = 15)
    @ApiModelProperty(value = "验收人员")
    private java.lang.String acceptancePersonnel;
    /**验收说明*/
    @Excel(name = "验收说明", width = 15)
    @ApiModelProperty(value = "验收说明")
    private java.lang.String acceptanceInstructions;
    /**验收图片*/
    @Excel(name = "验收图片", width = 15)
    @ApiModelProperty(value = "验收图片")
    private java.lang.String acceptanceImage;
    /**验收时间*/
    @Excel(name = "验收时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "验收时间")
    private java.util.Date acceptanceTime;
    /**出库人员*/
    @Excel(name = "出库人员", width = 15)
    @ApiModelProperty(value = "出库人员")
    private java.lang.String outboundPersonnel;
    /**出库时间*/
    @Excel(name = "出库时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出库时间")
    private java.util.Date outboundTime;
	/**使用时间*/
	@Excel(name = "使用时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用时间")
    private java.util.Date useTime;
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
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createPerson;
	/**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updatePerson;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    @Excel(name = "上传的图片", width = 15)
    @ApiModelProperty(value = "上传的图片")
    private String images;
}
