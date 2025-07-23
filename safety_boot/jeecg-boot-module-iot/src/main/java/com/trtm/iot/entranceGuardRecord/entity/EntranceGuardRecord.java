package com.trtm.iot.entranceGuardRecord.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 门禁考勤表信息
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
@Data
@TableName("entrance_guard_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="entrance_guard_record对象", description="门禁考勤表信息")
public class EntranceGuardRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**recordid*/
    @ApiModelProperty(value = "recordid")
    private java.lang.Integer recordid;
	/**types*/
	@Excel(name = "types", width = 15)
    @ApiModelProperty(value = "types")
    private java.lang.Integer types;
	/**openid*/
	@Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "openid")
    private java.lang.String openid;
	/**opentime*/
	@Excel(name = "打卡时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "opentime")
    private java.util.Date opentime;
	/**doorid*/
    @ApiModelProperty(value = "doorid")
    private java.lang.String doorid;
	/**info*/
    @ApiModelProperty(value = "info")
    private java.lang.String info;
	/**adminid*/
    @ApiModelProperty(value = "adminid")
    private java.lang.Integer adminid;
	/**pic*/
    @ApiModelProperty(value = "pic")
    private java.lang.String pic;
	/**adddate*/
	@Excel(name = "下班打卡时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "adddate")
    private java.util.Date adddate;
	/**isoffline*/
    @ApiModelProperty(value = "isoffline")
    private java.lang.Integer isoffline;
	/**capturepic*/
    @ApiModelProperty(value = "capturepic")
    private java.lang.String capturepic;
	/**isopen*/
    @ApiModelProperty(value = "isopen")
    private java.lang.Integer isopen;
	/**serialno*/
    @ApiModelProperty(value = "serialno")
    private java.lang.String serialno;
	/**isupload*/
    @ApiModelProperty(value = "isupload")
    private java.lang.Integer isupload;
	/**uploaddate*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "uploaddate")
    private java.util.Date uploaddate;
	/**temperature*/
    @ApiModelProperty(value = "temperature")
    private java.lang.String temperature;
	/**standard*/
    @ApiModelProperty(value = "standard")
    private java.lang.String standard;
	/**cid*/
    @ApiModelProperty(value = "cid")
    private java.lang.String cid;
	/**names*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "names")
    private java.lang.String names;
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**shebeino*/
	@Excel(name = "设备名称", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "shebeino")
    private java.lang.String shebeino;
    @ApiModelProperty(value = "DepartmentID")
    private java.lang.String departmentid;
    @Excel(name = "打卡地点", width = 15)
    @ApiModelProperty(value = "DepartName")
    private java.lang.String departname;
    @Dict(dicCode = "DingTalkstatus")
    private java.lang.Integer status;

    @TableField("startOrEnd")
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
}
