package com.trtm.iot.rebarTaskChecklist.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.rebarComponent.vo.RebarComponentTaskVo;
import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
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
import java.util.Date;
import java.util.List;

/**
 * @Description: rebar_task_checklist
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ApiModel(value="rebar_Task对象", description="rebar_Task对象")
public class TaskCheckVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**构件集合*/
    @Excel(name = "构件集合", width = 15)
    @ApiModelProperty(value = "构件集合")
    private List<RebarComponentTaskVo> rebarComponentTaskList;

    /**构件编号*/
    @Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "构件编号")
    private java.lang.String componentIds;
    /**构件数量*/
    @Excel(name = "构件数量", width = 15)
    @ApiModelProperty(value = "构件数量")
    private String actualComponentNumber;
    /**班组长编号*/
    @Excel(name = "班组长编号", width = 15)
    @ApiModelProperty(value = "班组长编号")
    private java.lang.String teamLeaderId;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**任务编号*/
	@Excel(name = "任务编号", width = 15)
    @ApiModelProperty(value = "任务编号")
    private String taskId;

    /**加工厂编号*/
    @Excel(name = "加工厂编号", width = 15)
    @ApiModelProperty(value = "加工厂编号")
    private String factoryId;
    /**加工厂编号*/
    @Excel(name = "加工厂编号", width = 15)
    @ApiModelProperty(value = "加工厂编号")
    private String factoryName;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String constructionSite;
	/**班组长*/
	@Excel(name = "班组长", width = 15)
    @ApiModelProperty(value = "班组长")
    private String teamLeader;
	/**申请人*/
	@Excel(name = "申请人", width = 15)
    @ApiModelProperty(value = "申请人")
    private String applicant;
	/**申请单位*/
	@Excel(name = "申请单位", width = 15)
    @ApiModelProperty(value = "申请单位")
    private String applicantUnit;
    /**加工负责人*/
    @Excel(name = "加工负责人", width = 15)
    @ApiModelProperty(value = "加工负责人")
    private String processors;
    /**组织机构编码*/
    @Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCode;
    /**分部分项机构编码*/
    @Excel(name = "分部分项机构编码", width = 15)
    @ApiModelProperty(value = "分部分项机构编码")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCodes;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "rebar_task_status")
    private String status;
	/**使用时间*/
	@Excel(name = "使用时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用时间")
    private Date useTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**逻辑删除标识*/
	@Excel(name = "逻辑删除标识", width = 15)
    @ApiModelProperty(value = "逻辑删除标识")
    private Integer isDeleted;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createPerson;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String updatePerson;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @Excel(name = "上传的图片", width = 15)
    @ApiModelProperty(value = "上传的图片")
    private List<String> images;
    @Excel(name = "上传的图片", width = 15)
    @ApiModelProperty(value = "上传的图片")
    private String image;
}
