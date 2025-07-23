package com.trtm.iot.company_contract_management.entity;

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
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2024-07-10
 * @Version: V1.0
 */
@Data
@TableName("company_contract_management")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_contract_management对象", description="合同管理")
public class CompanyContractManagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**项目编码*/
    @ApiModelProperty(value = "项目编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**产品类别*/
	@Excel(name = "产品类别", width = 15)
    @ApiModelProperty(value = "产品类别")
    private java.lang.String productcategory;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
    private java.lang.String customername;
	/**客户id*/
	@Excel(name = "客户id", width = 15)
    @ApiModelProperty(value = "客户id")
    @Dict(dictTable = "company_customer", dicText = "customer", dicCode = "id")
    private java.lang.String customerid;
	/**合同名称*/
	@Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private java.lang.String contractname;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
    @ApiModelProperty(value = "合同号")
    private java.lang.String contractnumber;
	/**合同类别（软件、硬件、服务、综合）*/
	@Excel(name = "合同类别（软件、硬件、服务、综合）", width = 15)
    @ApiModelProperty(value = "合同类别（软件、硬件、服务、综合）")
    private java.lang.String contracttype;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
    private java.lang.String ordernumber;
	/**甲方名称*/
	@Excel(name = "甲方名称", width = 15)
    @ApiModelProperty(value = "甲方名称")
    private java.lang.String partybname;
	/**乙方名称*/
	@Excel(name = "乙方名称", width = 15)
    @ApiModelProperty(value = "乙方名称")
    private java.lang.String partyaname;
	/**签订日期*/
	@Excel(name = "签订日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "签订日期")
    private java.util.Date signingdate;
	/**合同总额*/
	@Excel(name = "合同总额", width = 15)
    @ApiModelProperty(value = "合同总额")
    private java.math.BigDecimal totalamount;
	/**合同有效期*/
	@Excel(name = "合同有效期", width = 15)
    @ApiModelProperty(value = "合同有效期")
    private java.lang.String contractduration;
	/**客户联系人*/
	@Excel(name = "客户联系人", width = 15)
    @ApiModelProperty(value = "客户联系人")
    private java.lang.String customercontactperson;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String contactphonenumber;
	/**项目经理（销售或公司安排的负责人）*/
	@Excel(name = "项目经理（销售或公司安排的负责人）", width = 15)
    @ApiModelProperty(value = "项目经理（销售或公司安排的负责人）")
    private java.lang.String projectmanager;
	/**项目技术负责人（实施）*/
	@Excel(name = "项目技术负责人（实施）", width = 15)
    @ApiModelProperty(value = "项目技术负责人（实施）")
    private java.lang.String implementation;
	/**执行进度（%）*/
	@Excel(name = "执行进度（%）", width = 15)
    @ApiModelProperty(value = "执行进度（%）")
    private java.math.BigDecimal schedule;
	/**交付时间最终要求*/
	@Excel(name = "交付时间最终要求", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "交付时间最终要求")
    private java.util.Date deliverytime;
	/**交付地点*/
	@Excel(name = "交付地点", width = 15)
    @ApiModelProperty(value = "交付地点")
    private java.lang.String deliverylocation;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date creattime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String creatperson;
    private java.lang.String file;
    @Dict(dictTable = "company_project", dicText = "projectname", dicCode = "id")
    private java.lang.String ssxm;
    private java.lang.Integer number;
    private java.lang.String type;
}
