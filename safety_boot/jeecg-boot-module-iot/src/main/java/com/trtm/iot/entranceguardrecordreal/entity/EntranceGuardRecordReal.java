package com.trtm.iot.entranceguardrecordreal.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: 门禁考勤实时表
 * @Author: jeecg-boot
 * @Date:   2021-09-27
 * @Version: V1.0
 */
@Data
@TableName("entrance_guard_record_real")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="entrance_guard_record_real对象", description="门禁考勤实时表")
public class EntranceGuardRecordReal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**recordid*/
	@Excel(name = "recordid", width = 15)
    @ApiModelProperty(value = "recordid")
    private java.lang.Integer recordid;
	/**types*/
	@Excel(name = "types", width = 15)
    @ApiModelProperty(value = "types")
    private java.lang.Integer types;
//    @Excel(name = "type", width = 15)
//    @ApiModelProperty(value = "types")
//    private java.lang.Integer type;
	/**openid*/
	@Excel(name = "openid", width = 15)
    @ApiModelProperty(value = "openid")
    private java.lang.String openid;
	/**开门时间*/
	@Excel(name = "开门时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开门时间")
    private java.util.Date opentime;
	/**doorid*/
	@Excel(name = "doorid", width = 15)
    @ApiModelProperty(value = "doorid")
    private java.lang.String doorid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String info;
	/**adminid*/
	@Excel(name = "adminid", width = 15)
    @ApiModelProperty(value = "adminid")
    private java.lang.Integer adminid;
	/**pic*/
	@Excel(name = "pic", width = 15)
    @ApiModelProperty(value = "pic")
    private java.lang.String pic;
	/**adddate*/
	@Excel(name = "adddate", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "adddate")
    @TableField(value = "AddDate",updateStrategy = FieldStrategy.IGNORED)
    private java.util.Date adddate;
	/**isoffline*/
	@Excel(name = "isoffline", width = 15)
    @ApiModelProperty(value = "isoffline")
    private java.lang.Integer isoffline;
	/**capturepic*/
	@Excel(name = "capturepic", width = 15)
    @ApiModelProperty(value = "capturepic")
    private java.lang.String capturepic;
	/**isopen*/
	@Excel(name = "isopen", width = 15)
    @ApiModelProperty(value = "isopen")
    private java.lang.Integer isopen;
	/**serialno*/
	@Excel(name = "serialno", width = 15)
    @ApiModelProperty(value = "serialno")
    private java.lang.String serialno;
	/**isupload*/
	@Excel(name = "isupload", width = 15)
    @ApiModelProperty(value = "isupload")
    private java.lang.Integer isupload;
	/**uploaddate*/
	@Excel(name = "uploaddate", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "uploaddate")
    private java.util.Date uploaddate;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.String temperature;
	/**standard*/
	@Excel(name = "standard", width = 15)
    @ApiModelProperty(value = "standard")
    private java.lang.String standard;
	/**用户ID*/
	@Excel(name = "用户ID", width = 15)
    @ApiModelProperty(value = "用户ID")
    private java.lang.String cid;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String names;
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**部门ID*/
	@Excel(name = "部门ID", width = 15)
    @ApiModelProperty(value = "部门ID")
    private java.lang.String departmentid;
	/**部门名称*/
	@Excel(name = "部门名称", width = 15)
    @ApiModelProperty(value = "部门名称")
    private java.lang.String departname;
    @Dict(dicCode = "DingTalkstatus")
    private java.lang.Integer status;

    @TableField(exist = false)
    @ApiModelProperty(value = "打卡类型(上/下班) 0：上班打卡 1：下班打卡")
    private Integer startOrEnd;

    @Excel(name = "上班打卡时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "startTime")
    @TableField(exist = false)
    private java.util.Date startTime;

    @Excel(name = "下班打卡时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "endTime")
    @TableField(exist = false)
    private java.util.Date endTime;
    @ApiModelProperty(value = "隧道id", dataType = "String")
    private String tunnelId;

    @ApiModelProperty(value = "隧道名称", dataType = "String")
    private String tunnelName;
    @ApiModelProperty(value = "业务扩展字段", dataType = "String")
    private String extension;

    @ApiModelProperty(value = "逻辑删除标识", dataType = "Integer")
    @TableField(fill = FieldFill.INSERT)
    private Integer isDeleted;

    @ApiModelProperty(value = "创建人", dataType = "String")
    @TableField(fill = FieldFill.INSERT)
    private String createPerson;

    @ApiModelProperty(value = "创建时间", dataType = "Date")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
