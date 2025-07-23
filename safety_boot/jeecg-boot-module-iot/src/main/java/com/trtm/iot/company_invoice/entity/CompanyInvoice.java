package com.trtm.iot.company_invoice.entity;

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
 * @Description: 开票管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
@Data
@TableName("company_invoice")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_invoice对象", description="开票管理")
public class CompanyInvoice implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**所属项目*/
	@Excel(name = "所属项目", width = 15)
    @ApiModelProperty(value = "所属项目")
    @Dict(dictTable = "company_project", dicText = "projectname", dicCode = "id")
    private java.lang.String ssxm;
	/**发票编号*/
	@Excel(name = "发票编号", width = 15)
    @ApiModelProperty(value = "发票编号")
    private java.lang.String invoiceNumber;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
    @ApiModelProperty(value = "合同号")
    private java.lang.String contractNumber;
	/**客户id*/
	@Excel(name = "客户id", width = 15)
    @ApiModelProperty(value = "客户id")
    @Dict(dictTable = "company_customer", dicText = "customer", dicCode = "id")
    private java.lang.String customerid;
	/**开票日期*/
	@Excel(name = "开票日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开票日期")
    private java.util.Date invoiceDate;
	/**开票金额（不含税）*/
	@Excel(name = "开票金额（不含税）", width = 15)
    @ApiModelProperty(value = "开票金额（不含税）")
    private java.math.BigDecimal amount;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxRate;
	/**税额（amount * tax_rate）*/
	@Excel(name = "税额（amount * tax_rate）", width = 15)
    @ApiModelProperty(value = "税额（amount * tax_rate）")
    private java.math.BigDecimal taxAmount;
	/**含税总金额（amount + tax_amount）*/
	@Excel(name = "含税总金额（amount + tax_amount）", width = 15)
    @ApiModelProperty(value = "含税总金额（amount + tax_amount）")
    private java.math.BigDecimal totalAmount;
	/**货币类型（如CNY/USD）*/
	@Excel(name = "货币类型（如CNY/USD）", width = 15)
    @ApiModelProperty(value = "货币类型（如CNY/USD）")
    private java.lang.String currency;
	/**状态：0待开票/1已开票/2已寄送/3已付款/4已取消*/
	@Excel(name = "状态：0待开票/1已开票/2已寄送/3已付款/4已取消", width = 15)
    @ApiModelProperty(value = "状态：0待开票/1已开票/2已寄送/3已付款/4已取消")
    @Dict(dicCode="invstatus")
    private java.lang.Integer status;
	/**付款方式（银行转账、支付宝等）*/
	@Excel(name = "付款方式（银行转账、支付宝等）", width = 15)
    @ApiModelProperty(value = "付款方式（银行转账、支付宝等）")
    @Dict(dicCode = "paytype")
    private java.lang.String paymentMethod;
	/**付款截止日期*/
	@Excel(name = "付款截止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "付款截止日期")
    private java.util.Date dueDate;
	/**实际付款日期*/
	@Excel(name = "实际付款日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际付款日期")
    private java.util.Date paymentDate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**附件路径*/
	@Excel(name = "附件路径", width = 15)
    @ApiModelProperty(value = "附件路径")
    private java.lang.String invfile;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createdBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createdAt;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updatedAt;
}
