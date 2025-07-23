package com.trtm.iot.staffbase.entity;

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
 * @Description: staff_base_salary
 * @Author: jeecg-boot
 * @Date:   2025-02-25
 * @Version: V1.0
 */
@Data
@TableName("staff_base_salary")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="staff_base_salary对象", description="staff_base_salary")
public class StaffBaseSalary implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String username;
	/**发放月度*/
	@Excel(name = "发放月度", width = 15)
    @ApiModelProperty(value = "发放月度")
    private java.lang.String workmonth;
	/**发放银行*/
	@Excel(name = "发放银行", width = 15)
    @ApiModelProperty(value = "发放银行")
    private java.lang.String paybank;
	/**发放薪资*/
	@Excel(name = "发放薪资", width = 15)
    @ApiModelProperty(value = "发放薪资")
    private java.math.BigDecimal paysalary;
	/**月度工作时长*/
	@Excel(name = "月度工作时长", width = 15)
    @ApiModelProperty(value = "月度工作时长")
    private java.lang.String payhours;
	/**打卡天数*/
	@Excel(name = "打卡天数", width = 15)
    @ApiModelProperty(value = "打卡天数")
    private java.lang.String paydays;
	/**证件号码*/
	@Excel(name = "证件号码", width = 15)
    @ApiModelProperty(value = "证件号码")
    private java.lang.String shenfenz;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**人员类型*/
	@Excel(name = "人员类型", width = 15)
    @ApiModelProperty(value = "人员类型")
    private java.lang.String renyuantype;
	/**班组*/
	@Excel(name = "班组", width = 15)
    @ApiModelProperty(value = "班组")
    private java.lang.String banzu;
	/**工资单附件*/
	@Excel(name = "工资单附件", width = 15)
    @ApiModelProperty(value = "工资单附件")
    private java.lang.String paydetail;
	/**结算方式*/
	@Excel(name = "结算方式", width = 15)
    @ApiModelProperty(value = "结算方式")
    private java.lang.String payway;
    // 是否发放 0已登记 未审核，5已审核，10已发放
    private java.lang.Integer payoff;

}
