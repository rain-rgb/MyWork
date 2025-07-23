package com.trtm.iot.company_order_management.vo;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagementS;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
@Data
@ApiModel(value="company_order_managementPage对象", description="订单管理")
public class CompanyOrderManagementPage {

	/**id*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private Integer id;
	/**guid*/
	@Excel(name = "guid", width = 15)
	@ApiModelProperty(value = "guid")
	private String guid;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
	@ApiModelProperty(value = "项目名称")
	private String projectname;
	/**产品类别*/
	@Excel(name = "产品类别", width = 15)
	@ApiModelProperty(value = "产品类别")
	private String productcategory;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
	@ApiModelProperty(value = "客户名称")
	private String customername;
	@Dict(dictTable ="company_customer", dicText = "customer", dicCode = "id")
	private String customerid;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
	@ApiModelProperty(value = "合同号")
	private String contractnumber;
	/**订单名称*/
	@Excel(name = "订单名称", width = 15)
	@ApiModelProperty(value = "订单名称")
	private String ordername;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
	@ApiModelProperty(value = "订单编号")
	private String ordernumber;
	/**订货方名称*/
	@Excel(name = "订货方名称", width = 15)
	@ApiModelProperty(value = "订货方名称")
	private String orderingpartyname;
	/**供货方名称*/
	@Excel(name = "供货方名称", width = 15)
	@ApiModelProperty(value = "供货方名称")
	private String suppliername;
	/**签订日期*/
	@Excel(name = "签订日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "签订日期")
	private Date signingdate;
	/**订单总计金额*/
	@Excel(name = "订单总计金额", width = 15)
	@ApiModelProperty(value = "订单总计金额")
	private java.math.BigDecimal totalamount;
	/**客户联系人*/
	@Excel(name = "客户联系人", width = 15)
	@ApiModelProperty(value = "客户联系人")
	private String customercontactperson;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
	@ApiModelProperty(value = "联系电话")
	private String contactphonenumber;
	/**项目经理（销售或公司安排的负责人）*/
	@Excel(name = "项目经理（销售或公司安排的负责人）", width = 15)
	@ApiModelProperty(value = "项目经理（销售或公司安排的负责人）")
	private String projectmanager;
	/**项目技术负责人（实施）*/
	@Excel(name = "项目技术负责人（实施）", width = 15)
	@ApiModelProperty(value = "项目技术负责人（实施）")
	private String implementation;
	/**交付时间最终要求*/
	@Excel(name = "交付时间最终要求", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "交付时间最终要求")
	private Date deliverytime;
	/**交付地点*/
	@Excel(name = "交付地点", width = 15)
	@ApiModelProperty(value = "交付地点")
	private String deliverylocation;
	private Date creattime;
	private String sysOrgCode;
	private String creatperson;
	private String file;
	@Dict(dictTable = "company_project", dicText = "projectname", dicCode = "id")
	private java.lang.String ssxm;

	@ExcelCollection(name="订单管理子表")
	@ApiModelProperty(value = "订单管理子表")
	private List<CompanyOrderManagementS> companyOrderManagementSList;

}
