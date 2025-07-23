package com.trtm.sy.syRegistered.entity;

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
 * @Description: sy_registered
 * @Author: jeecg-boot
 * @Date: 2022-09-22
 * @Version: V1.0
 */
@Data
@TableName("sy_registered")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_registered对象", description = "sy_registered")
public class SyRegistered implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 检测项目（0：水泥胶砂强度）
     */
    @Excel(name = "检测项目（0：水泥胶砂强度）", width = 15)
    @ApiModelProperty(value = "检测项目（0：水泥胶砂强度）")
    @Dict(dicCode = "checkproject")
    private Integer checkproject;
    /**
     * 责任人
     */
    @Excel(name = "责任人", width = 15)
    @ApiModelProperty(value = "责任人")
    private String owner;
    /**
     * 不合格类型（0：主动；1：被动）
     */
    @Excel(name = "不合格类型（0：主动；1：被动）", width = 15)
    @ApiModelProperty(value = "不合格类型（0：主动；1：被动）")
    @Dict(dicCode = "buhegetype")
    private Integer buhegetype;
    /**
     * 时间
     */
    @Excel(name = "发现不合格时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发现不合格时间")
    private Date time;
    /**
     * 原因描述
     */
    @Excel(name = "原因描述", width = 15)
    @ApiModelProperty(value = "原因描述")
    private String reason;
    /**
     * 罚款奖励金额数
     */
    @Excel(name = "罚款奖励金额数", width = 15)
    @ApiModelProperty(value = "罚款奖励金额数")
    private String amount;
    /**
     * 订单所属组织机构
     */
    @Excel(name = "订单所属组织机构", width = 15)
    @ApiModelProperty(value = "订单所属组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysorgcode;

    @Excel(name = "闭合状态（0:未闭合；1:已闭合）", width = 15)
    @ApiModelProperty(value = "闭合状态（0:未闭合；1:已闭合）")
    private Integer closestatus;

    @Excel(name = "闭合时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "闭合时间")
    private Date closetime;
    @Excel(name = "闭合人", width = 15)
    @ApiModelProperty(value = "闭合人")
    private String closeren;
    @Excel(name = "闭合措施", width = 15)
    @ApiModelProperty(value = "闭合措施")
    private String closesteps;
    @Excel(name = "登记人", width = 15)
    @ApiModelProperty(value = "登记人")
    private String registerren;
    @Excel(name = "登记时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "登记时间")
    private Date registertime;

}
