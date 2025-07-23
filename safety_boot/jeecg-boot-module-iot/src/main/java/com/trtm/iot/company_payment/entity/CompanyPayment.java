package com.trtm.iot.company_payment.entity;

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
 * @Description: 回款管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
@Data
@TableName("company_payment")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_payment对象", description="回款管理")
public class CompanyPayment implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**回款编号*/
	@Excel(name = "回款编号", width = 15)
    @ApiModelProperty(value = "回款编号")
    private java.lang.String paymentNumber;
	/**所属项目*/
	@Excel(name = "所属项目", width = 15)
    @ApiModelProperty(value = "所属项目")
    @Dict(dictTable = "company_project", dicText = "projectname", dicCode = "id")
    private java.lang.String ssxm;
	/**客户id*/
	@Excel(name = "客户id", width = 15)
    @ApiModelProperty(value = "客户id")
    @Dict(dictTable = "company_customer", dicText = "customer", dicCode = "id")
    private java.lang.String customerid;
	/**开票编号*/
	@Excel(name = "开票编号", width = 15)
    @ApiModelProperty(value = "开票编号")
    private java.lang.String invoiceNumber;
	/**实际到账金额*/
	@Excel(name = "实际到账金额", width = 15)
    @ApiModelProperty(value = "实际到账金额")
    private java.math.BigDecimal paymentAmount;
    /**实际到账金额*/
    @Excel(name = "开票金额", width = 15)
    @ApiModelProperty(value = "开票金额")
    private java.math.BigDecimal amount;
	/**货币类型*/
	@Excel(name = "货币类型", width = 15)
    @ApiModelProperty(value = "货币类型")
    private java.lang.String currency;
	/**汇率*/
	@Excel(name = "汇率", width = 15)
    @ApiModelProperty(value = "汇率")
    private java.lang.String exchangeRate;
	/**到账日期*/
	@Excel(name = "到账日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "到账日期")
    private java.util.Date paymentDate;
	/**支付方式*/
	@Excel(name = "支付方式", width = 15)
    @ApiModelProperty(value = "支付方式")
    @Dict(dicCode = "paytype")
    private java.lang.String paymentMethod;
	/**状态：0待确认/1已到账/2部分到账/3已核销/4争议中*/
	@Excel(name = "状态：0待确认/1已到账/2部分到账/3已核销/4争议中", width = 15)
    @ApiModelProperty(value = "状态：0待确认/1已到账/2部分到账/3已核销/4争议中")
    @Dict(dicCode="paystatus")
    private java.lang.Integer status;
	/**客户付款账户信息*/
	@Excel(name = "客户付款账户信息", width = 15)
    @ApiModelProperty(value = "客户付款账户信息")
    private java.lang.String bankAccount;
	/**备注（如付款说明、争议原因）*/
	@Excel(name = "备注（如付款说明、争议原因）", width = 15)
    @ApiModelProperty(value = "备注（如付款说明、争议原因）")
    private java.lang.String remarks;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String payfile;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createdBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createdAt;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updatedAt;
}
