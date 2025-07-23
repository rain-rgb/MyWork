package com.trtm.iot.projoverview.entity;

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
 * @Description: proj_overview
 * @Author: jeecg-boot
 * @Date: 2023-11-03
 * @Version: V1.0
 */
@Data
@TableName("proj_overview")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "proj_overview对象", description = "proj_overview")
public class ProjOverview implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目概览id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "项目概览id")
    private java.lang.Integer id;
    /**
     * 组织机构
     */
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projName;
    /**
     * 项目描述
     */
    @Excel(name = "项目描述", width = 15)
    @ApiModelProperty(value = "项目描述")
    private java.lang.String projDescribe;
    /**
     * 道路等级
     */
    @Excel(name = "道路等级", width = 15)
    @ApiModelProperty(value = "道路等级")
    private java.lang.String roadGrade;
    /**
     * 道路里程
     */
    @Excel(name = "道路里程", width = 15)
    @ApiModelProperty(value = "道路里程")
    private java.lang.String roadMileage;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String beizhu;
    /**
     * 说明
     */
    @Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String illustrate;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
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
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * 图片url
     */
    @Excel(name = "图片url", width = 15)
    @ApiModelProperty(value = "图片url")
    private java.lang.String pictureUrl;
    /**
     * 文件url
     */
    @Excel(name = "文件url", width = 15)
    @ApiModelProperty(value = "文件url")
    private java.lang.String fileUrl;
}
