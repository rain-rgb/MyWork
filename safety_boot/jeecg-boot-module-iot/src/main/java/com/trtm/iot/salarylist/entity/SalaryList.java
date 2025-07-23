package com.trtm.iot.salarylist.entity;

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
 * @Description: 工资清单
 * @Author: jeecg-boot
 * @Date:   2022-07-13
 * @Version: V1.0
 */
@Data
@TableName("salary_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="salary_list对象", description="工资清单")
public class SalaryList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**权限*/
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**支付日期*/
	@Excel(name = "支付日期", width = 15, format = "yyyy-MM")
    @ApiModelProperty(value = "支付日期")
    private java.lang.String paymentDate;
	/**应付工资*/
	@Excel(name = "应付工资", width = 15)
    @ApiModelProperty(value = "应付工资")
    private java.lang.String payableWages;
	/**已付工资*/
	@Excel(name = "已付工资", width = 15)
    @ApiModelProperty(value = "已付工资")
    private java.lang.String salaryPaid;
	/**支付比例*/
	@Excel(name = "支付比例", width = 15)
    @ApiModelProperty(value = "支付比例")
    private java.lang.String payoutRatio;
}
