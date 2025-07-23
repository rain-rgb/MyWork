package com.trtm.iot.investmentchecklist.entity;

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
 * @Description: 投资清单
 * @Author: jeecg-boot
 * @Date:   2022-07-11
 * @Version: V1.0
 */
@Data
@TableName("investment_checklist")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="investment_checklist对象", description="投资清单")
public class InvestmentChecklist implements Serializable {
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
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String uuid;
	/**总投资*/
	@Excel(name = "总投资", width = 15)
    @ApiModelProperty(value = "总投资")
    private java.lang.String totalInvestment;
	/**总累计投资*/
	@Excel(name = "总累计投资", width = 15)
    @ApiModelProperty(value = "总累计投资")
    private java.lang.String cumulativeInvestment;
	/**总进度*/
	@Excel(name = "总进度", width = 15)
    @ApiModelProperty(value = "总进度")
    private java.lang.String schedule;
	/**年总投资*/
	@Excel(name = "年总投资", width = 15)
    @ApiModelProperty(value = "年总投资")
    private java.lang.String annualInvestment;
	/**年累计投资*/
	@Excel(name = "年累计投资", width = 15)
    @ApiModelProperty(value = "年累计投资")
    private java.lang.String annualCuminvestment;
	/**年进度*/
	@Excel(name = "年进度", width = 15)
    @ApiModelProperty(value = "年进度")
    private java.lang.String annualSchedule;
    /**项目名称*/
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
}
