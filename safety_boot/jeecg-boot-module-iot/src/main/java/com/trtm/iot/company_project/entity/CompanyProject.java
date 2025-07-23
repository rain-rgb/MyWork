package com.trtm.iot.company_project.entity;

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
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2025-03-10
 * @Version: V1.0
 */
@Data
@TableName("company_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_project对象", description="项目管理")
public class CompanyProject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构编码*/
	@Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgcode;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**甲方名称*/
	@Excel(name = "甲方名称", width = 15)
    @ApiModelProperty(value = "甲方名称")
    private java.lang.String partyaname;
	/**项目类型（公路/铁路/水运/地铁/市域轨道/水利）*/
	@Excel(name = "项目类型（公路/铁路/水运/地铁/市域轨道/水利）", width = 15)
    @ApiModelProperty(value = "项目类型（公路/铁路/水运/地铁/市域轨道/水利）")
    private java.lang.String projecttype;
	/**营销类别（直销/渠道合作）*/
	@Excel(name = "营销类别（直销/渠道合作）", width = 15)
    @ApiModelProperty(value = "营销类别（直销/渠道合作）")
    private java.lang.String markettype;
	/**订单个数*/
	@Excel(name = "订单个数", width = 15)
    @ApiModelProperty(value = "订单个数")
    private java.lang.Integer ordernumber;
	/**订单总金额（元）*/
	@Excel(name = "订单总金额（元）", width = 15)
    @ApiModelProperty(value = "订单总金额（元）")
    private java.math.BigDecimal orderamount;
	/**合同个数*/
	@Excel(name = "合同个数", width = 15)
    @ApiModelProperty(value = "合同个数")
    private java.lang.Integer totalnumber;
	/**合同总金额（元）*/
	@Excel(name = "合同总金额（元）", width = 15)
    @ApiModelProperty(value = "合同总金额（元）")
    private java.math.BigDecimal totalamount;
	/**开票总金额（元）*/
	@Excel(name = "开票总金额（元）", width = 15)
    @ApiModelProperty(value = "开票总金额（元）")
    private java.math.BigDecimal invoicamount;
	/**回款总金额（元）*/
	@Excel(name = "回款总金额（元）", width = 15)
    @ApiModelProperty(value = "回款总金额（元）")
    private java.math.BigDecimal paymentamount;
	/**最终交付日期要求*/
	@Excel(name = "最终交付日期要求", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "最终交付日期要求")
    private java.util.Date deliverytime;
    /**最终交付日期要求*/
    @Excel(name = "签订日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "签订日期")
    private java.util.Date signedtime;
	/**最终完成日期*/
	@Excel(name = "最终完成日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "最终完成日期")
    private java.util.Date completetime;
	/**实施进度百分比*/
	@Excel(name = "实施进度百分比", width = 15)
    @ApiModelProperty(value = "实施进度百分比")
    private java.math.BigDecimal progresspercentage;
	/**进度状态（提前/正常/滞后）*/
	@Excel(name = "进度状态（提前/正常/滞后）", width = 15)
    @ApiModelProperty(value = "进度状态（提前/正常/滞后）")
    @Dict(dicCode = "progressstate")
    private java.lang.Integer progressstate;
	/**开票进度百分比*/
	@Excel(name = "开票进度百分比", width = 15)
    @ApiModelProperty(value = "开票进度百分比")
    private java.math.BigDecimal invoicpercentage;
	/**回款进度百分比*/
	@Excel(name = "回款进度百分比", width = 15)
    @ApiModelProperty(value = "回款进度百分比")
    private java.math.BigDecimal paymentpercentage;
	/**回款状态（提前/正常/滞后）*/
	@Excel(name = "回款状态（提前/正常/滞后）", width = 15)
    @ApiModelProperty(value = "回款状态（提前/正常/滞后）")
    @Dict(dicCode = "progressstate")
    private java.lang.Integer paymentstate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
}
