package com.trtm.iot.wzsubcontact.entity;

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
 * @Description: wzsubcontact_m
 * @Author: jeecg-boot
 * @Date: 2023-10-16
 * @Version: V1.0
 */
@Data
@TableName("wzsubcontact_m")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "wzsubcontact_m对象", description = "wzsubcontact_m")
public class WzsubcontactM implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**
     * 工程类型 1：临时工程 2：主体工程
     */
    @Excel(name = "工程类型", width = 15)
    @ApiModelProperty(value = "工程类型")
    private java.lang.Integer projType;
    /**
     * 公司名称
     */
    @Excel(name = "公司名称", width = 15)
    @ApiModelProperty(value = "公司名称")
    private java.lang.String companyName;
    /**
     * 准入证号
     */
    @Excel(name = "准入证号", width = 15)
    @ApiModelProperty(value = "准入证号")
    private java.lang.Integer accessCertificateNumber;
    /**
     * 代理人
     */
    @Excel(name = "代理人", width = 15)
    @ApiModelProperty(value = "代理人")
    private java.lang.String agent;
    /**
     * 联系方式
     */
    @Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String contactInformation;
    /**
     * 合同摘要
     */
    @Excel(name = "合同摘要", width = 15)
    @ApiModelProperty(value = "合同摘要")
    private java.lang.String contractSummary;
    /**
     * 合同类别
     */
    @Excel(name = "合同类别", width = 15)
    @ApiModelProperty(value = "合同类别")
    private java.lang.String contractCategory;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private java.lang.String contractNumber;
    /**
     * 签订日期
     */
    @Excel(name = "签订日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "签订日期")
    private java.util.Date signingDate;
    /**
     * 招标类型
     */
    @Excel(name = "招标类型", width = 15)
    @ApiModelProperty(value = "招标类型")
    private java.lang.String biddingType;
    /**
     * 不含税合同价款金额（元）
     */
    @Excel(name = "不含税合同价款金额（元）", width = 15)
    @ApiModelProperty(value = "不含税合同价款金额（元）")
    private java.lang.String excludingTaxAmount;
    /**
     * 增值税金额（元）
     */
    @Excel(name = "增值税金额（元）", width = 15)
    @ApiModelProperty(value = "增值税金额（元）")
    private java.lang.String valueAddedTax;
    /**
     * 履约保证缴纳金额（元）
     */
    @Excel(name = "履约保证缴纳金额（元）", width = 15)
    @ApiModelProperty(value = "履约保证缴纳金额（元）")
    private java.lang.String paymentAmount;
    /**
     * 履约进度，1、正在履约；
     * 2、已退场，已封账；
     * 3、已退场，未封账。
     */
    @Excel(name = "履约进度，1、正在履约；2、已退场，已封账；3、已退场，未封账。", width = 15)
    @ApiModelProperty(value = "履约进度，1、正在履约；2、已退场，已封账；3、已退场，未封账。")
    private java.lang.String remarks;
    /**
     * 供应商编码
     */
//    @Excel(name = "供应商编码", width = 15)
    @ApiModelProperty(value = "供应商编码")
    private java.lang.String supplier;
    /**
     * 组织机构
     */
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * guid
     */
//    @Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
    /**
     * firstParty
     */
//    @Excel(name = "firstParty", width = 15)
    @ApiModelProperty(value = "firstParty")
    private java.lang.String firstParty;
    /**
     * 合同文件
     */
//    @Excel(name = "合同文件", width = 15)
    @ApiModelProperty(value = "合同文件")
    private java.lang.String fileurl;
}
