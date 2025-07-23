package com.trtm.iot.anquanfxgk.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

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
 * @Description: anquan_fxfjgk_pingjia
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxfjgk_pingjia")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxfjgk_pingjia对象", description="anquan_fxfjgk_pingjia")
public class AnquanFxfjgkPingjia implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**项目*/
	@Excel(name = "项目", width = 15)
    @ApiModelProperty(value = "项目")
    private java.lang.String projectName;
	/**位置*/
	@Excel(name = "位置", width = 15)
    @ApiModelProperty(value = "位置")
    private java.lang.String position;
	/**风险*/
	@Excel(name = "风险", width = 15)
    @ApiModelProperty(value = "风险")
    private java.lang.String risk;
	/**相关单位*/
	@Excel(name = "相关单位", width = 15)
    @ApiModelProperty(value = "相关单位")
    private java.lang.String conherentUnit;
	/**管控负责人管控情况*/
	@Excel(name = "管控负责人管控情况", width = 15)
    @ApiModelProperty(value = "管控负责人管控情况")
    private java.lang.String managementHead;
	/**管控负责人管控情况评价*/
	@Excel(name = "管控负责人管控情况评价", width = 15)
    @ApiModelProperty(value = "管控负责人管控情况评价")
    private java.lang.String managementHeadPj;
	/**安全管理人员管控情况*/
	@Excel(name = "安全管理人员管控情况", width = 15)
    @ApiModelProperty(value = "安全管理人员管控情况")
    private java.lang.String securityManagement;
	/**安全管理人员管控情况评价*/
	@Excel(name = "安全管理人员管控情况评价", width = 15)
    @ApiModelProperty(value = "安全管理人员管控情况评价")
    private java.lang.String securityManagementPj;
	/**安全负责人管控情况*/
	@Excel(name = "安全负责人管控情况", width = 15)
    @ApiModelProperty(value = "安全负责人管控情况")
    private java.lang.String securityHead;
	/**安全负责人管控情况评价*/
	@Excel(name = "安全负责人管控情况评价", width = 15)
    @ApiModelProperty(value = "安全负责人管控情况评价")
    private java.lang.String securityHeadPj;
	/**主要负责人管控情况*/
	@Excel(name = "主要负责人管控情况", width = 15)
    @ApiModelProperty(value = "主要负责人管控情况")
    private java.lang.String head;
	/**主要负责人管控情况评价*/
	@Excel(name = "主要负责人管控情况评价", width = 15)
    @ApiModelProperty(value = "主要负责人管控情况评价")
    private java.lang.String headPj;
	/**单位管控情况*/
	@Excel(name = "单位管控情况", width = 15)
    @ApiModelProperty(value = "单位管控情况")
    private java.lang.String unitName;
	/**单位管控情况评价*/
	@Excel(name = "单位管控情况评价", width = 15)
    @ApiModelProperty(value = "单位管控情况评价")
    private java.lang.String unitPj;
	/**总体评价*/
	@Excel(name = "总体评价", width = 15)
    @ApiModelProperty(value = "总体评价")
    private java.lang.String totalityPj;
	/**评价人*/
	@Excel(name = "评价人", width = 15)
    @ApiModelProperty(value = "评价人")
    private java.lang.String pjName;
	/**评价时间*/
	@Excel(name = "评价时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "评价时间")
    private java.util.Date pjTime;
	/**主表guid*/
	@Excel(name = "主表guid", width = 15)
    @ApiModelProperty(value = "主表guid")
    private java.lang.String baseGuid;

    /**安全风险评价-明细表*/
    @TableField(exist = false)
    @ApiModelProperty(value = "安全风险评价-明细表")
    private List<AnquanFxfjgkPingjiaDetail> anquanFxfjgkPingjiaDetails;


}
