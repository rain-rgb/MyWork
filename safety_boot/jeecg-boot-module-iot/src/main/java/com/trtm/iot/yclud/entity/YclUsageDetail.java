package com.trtm.iot.yclud.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: ycl_usage_detail
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Data
@TableName("ycl_usage_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ycl_usage_detail对象", description="ycl_usage_detail")
public class YclUsageDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
    /**拌合站号*/
    @Excel(name = "拌合站号", width = 15)
    @ApiModelProperty(value = "拌合站号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
    /**配料时间（领料时间）*/
    @Excel(name = "配料时间（领料时间）", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "配料时间（领料时间）")
    private java.util.Date dosingTime;
    /**配料单号*/
    @Excel(name = "配料单号", width = 15)
    @ApiModelProperty(value = "配料单号")
    private java.lang.String dosingOrderNumber;
    /**工程部位（名称）*/
    @Excel(name = "工程部位（名称）", width = 15)
    @ApiModelProperty(value = "工程部位（名称）")
    private java.lang.String projectPart;
    /**使用量*/
    @Excel(name = "使用量", width = 15)
    @ApiModelProperty(value = "使用量")
    private java.lang.String uses;
    /**料仓id*/
    @Excel(name = "料仓id", width = 15)
    @ApiModelProperty(value = "料仓id")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private java.lang.String storageId;
    /**检验批编号*/
    @Excel(name = "检验批编号", width = 15)
    @ApiModelProperty(value = "检验批编号")
    private java.lang.String inspectionLotNumber;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**任务单号*/
    @Excel(name = "任务单号", width = 15)
    @ApiModelProperty(value = "任务单号")
    private java.lang.String renwudan;
    /**领用原因*/
    @Excel(name = "领用原因", width = 15)
    @ApiModelProperty(value = "领用原因")
    private java.lang.String lingyongyuanyin;
    /**材料名称*/
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String storageName;
    /**分部分项id*/
    @Excel(name = "分部分项id", width = 15)
    @ApiModelProperty(value = "分部分项id")
    private java.lang.String treeid;
    /**分部分项code*/
    @Excel(name = "分部分项code", width = 15)
    @ApiModelProperty(value = "分部分项code")
    private java.lang.String code;
    /**材料编号*/
    @Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private java.lang.String cailiaono;
    /**规格型号*/
    @Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private java.lang.String guigexinghao;
    /**唯一键*/
    @Excel(name = "唯一键", width = 15)
    @ApiModelProperty(value = "唯一键")
    private java.lang.String guid;
    /**数据同步状态*/
    @Excel(name = "数据同步状态", width = 15)
    @ApiModelProperty(value = "数据同步状态")
    private java.lang.Integer status;
    /**0未删除1已删除*/
    @Excel(name = "0未删除1已删除", width = 15)
    @ApiModelProperty(value = "0未删除1已删除")
    @TableLogic(value = "0", delval = "1")
    private java.lang.Integer isdel;
    /***/
    @Excel(name = "", width = 15)
    @ApiModelProperty(value = "")
    private java.lang.String description;
}
