package com.trtm.iot.company_order_management.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 订单管理子表
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
@ApiModel(value="company_order_management_s对象", description="订单管理子表")
@Data
@TableName("company_order_management_s")
public class CompanyOrderManagementS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private String id;
	/**订单编号*/
    @ApiModelProperty(value = "订单编号")
    private String ordernumber;
	/**订购产品名称*/
	@Excel(name = "订购产品名称", width = 15)
    @ApiModelProperty(value = "订购产品名称")
    private String productname;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private String number;
	/**不含税单价*/
	@Excel(name = "不含税单价", width = 15)
    @ApiModelProperty(value = "不含税单价")
    private java.math.BigDecimal taxprice;
	/**含税单价*/
	@Excel(name = "含税单价", width = 15)
    @ApiModelProperty(value = "含税单价")
    private java.math.BigDecimal price;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxrate;
	/**小计金额*/
	@Excel(name = "小计金额", width = 15)
    @ApiModelProperty(value = "小计金额")
    private java.math.BigDecimal subtotal;
	/**订单扫描附件*/
	@Excel(name = "订单扫描附件", width = 15)
    @ApiModelProperty(value = "订单扫描附件")
    private String file;
    private String sgimage;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    private Date creattime;
    private String person;
    private String times;
    private Integer status;
}
