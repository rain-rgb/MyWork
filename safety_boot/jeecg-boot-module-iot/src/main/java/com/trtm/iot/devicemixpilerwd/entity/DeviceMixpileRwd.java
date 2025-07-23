package com.trtm.iot.devicemixpilerwd.entity;

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
 * @Description: 软基工单表
 * @Author: jeecg-boot
 * @Date:   2022-02-22
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_rwd")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_rwd对象", description="软基工单表")
public class DeviceMixpileRwd implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**软基工单号*/
	@Excel(name = "软基工单号", width = 15)
    @ApiModelProperty(value = "软基工单号")
    private java.lang.String rjrwd;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgcode;

    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String mileage;
	/**里程id*/
	@Excel(name = "里程id", width = 15)
    @ApiModelProperty(value = "里程id")
    private java.lang.String mileageid;
	/**设计桩基数*/
	@Excel(name = "设计桩基数", width = 15)
    @ApiModelProperty(value = "设计桩基数")
    private java.lang.Integer descount;
	/**设计每根桩长*/
	@Excel(name = "设计每根桩长", width = 15)
    @ApiModelProperty(value = "设计每根桩长")
    private java.lang.String deslen;
	/**通知人*/
	@Excel(name = "通知人", width = 15)
    @ApiModelProperty(value = "通知人")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String msgperson;
	/**开工日期*/
	@Excel(name = "开工日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开工日期")
    private java.util.Date starttime;
	/**截止日期*/
	@Excel(name = "截止日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止日期")
    private java.util.Date endtime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String other;
	/**工单状态，0：未开始，1：进行中；2：已完成，3：滞后*/
	@Excel(name = "工单状态，0：未开始，1：进行中；2：已完成，3：滞后", width = 15)
    @ApiModelProperty(value = "工单状态，0：未开始，1：进行中；2：已完成，3：滞后")
    private java.lang.Integer status;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**是否进行统计*/
	@Excel(name = "是否进行统计", width = 15)
    @ApiModelProperty(value = "是否进行统计")
    private java.lang.Integer alt;

    private String totalpro;// 进度
    private Integer piletotal; // 已完成桩基数
    private Integer mileagetotal; // 完成里程数
    private Integer chaobiaototal;// 超标数
    private Integer handled;// 处置数
    private Integer checked;// 审核数
    private String treeid;//里程节点id
    private String gxwbs;//里程节点id
}
