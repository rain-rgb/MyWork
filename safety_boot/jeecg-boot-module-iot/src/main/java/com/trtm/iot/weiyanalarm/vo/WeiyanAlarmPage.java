package com.trtm.iot.weiyanalarm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.weiyanalarmhandler.entity.WeiyanAlarmHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class WeiyanAlarmPage {
    /**主键id*/
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
    /**唯一ID*/
    @Excel(name = "唯一ID", width = 15)
    @ApiModelProperty(value = "唯一ID")
    private java.lang.String batchNo;
    /**围岩级别*/
    @Excel(name = "围岩级别", width = 15)
    @ApiModelProperty(value = "围岩级别")
    private java.lang.String wallRockLevel;
    /**测点/线*/
    @Excel(name = "测点/线", width = 15)
    @ApiModelProperty(value = "测点/线")
    private java.lang.String measuringPoint;
    /**测量时间*/
    @Excel(name = "测量时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "测量时间")
    private java.util.Date birthTime;
    /**变形速率(mm/d)*/
    @Excel(name = "变形速率(mm/d)", width = 15)
    @ApiModelProperty(value = "变形速率(mm/d)")
    private java.lang.Double deformationRate;
    /**累计变形量 单位（mm）*/
    @Excel(name = "累计变形量 单位（mm）", width = 15)
    @ApiModelProperty(value = "累计变形量 单位（mm）")
    private java.lang.Double accumulatedDeformation;
    /**报警级别(0,代表合格，1代表黄色报警，21代表红色报警)*/
    @Excel(name = "报警级别(0,代表合格，1代表黄色报警，21代表红色报警)", width = 15)
    @ApiModelProperty(value = "报警级别(0,代表合格，1代表黄色报警，21代表红色报警)")
    private java.lang.Integer callLevel;
    /**状态(0 未关闭 1 已关闭)*/
    @Excel(name = "状态(0 未关闭 1 已关闭)", width = 15)
    @ApiModelProperty(value = "状态(0 未关闭 1 已关闭)")
    private java.lang.Integer isUse;
    /**处置数*/
    @Excel(name = "处置数", width = 15)
    @ApiModelProperty(value = "处置数")
    private java.lang.Integer handleNum;
    /**工点*/
    @Excel(name = "工点", width = 15)
    @ApiModelProperty(value = "工点")
    private java.lang.String workPoint;
    /**断面名称*/
    @Excel(name = "断面名称", width = 15)
    @ApiModelProperty(value = "断面名称")
    private java.lang.String sectionName;
    /**工区*/
    @Excel(name = "工区", width = 15)
    @ApiModelProperty(value = "工区")
    private java.lang.String workArea;
    /**测量类型*/
    @Excel(name = "测量类型", width = 15)
    @ApiModelProperty(value = "测量类型")
    private java.lang.String measurementType;
    /**处置状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回*/
    @Excel(name = "处置状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回", width = 15)
    @ApiModelProperty(value = "处置状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回")
    private java.lang.Integer dispositionStatus;
    /**权限*/
    @ApiModelProperty(value = "权限")
    private java.lang.String sysOrgCode;
    /**测量人员*/
    @Excel(name = "测量人员", width = 15)
    @ApiModelProperty(value = "测量人员")
    private java.lang.String surveyor;
    /**速率阈值(mm/d)*/
    @Excel(name = "速率阈值(mm/d)", width = 15)
    @ApiModelProperty(value = "速率阈值(mm/d)")
    private java.lang.String rateThreshold;
    /**累计阈值(mm)*/
    @Excel(name = "累计阈值(mm)", width = 15)
    @ApiModelProperty(value = "累计阈值(mm)")
    private java.lang.String cumulativeHreshold;

    private WeiyanAlarmHandler weiyanAlarmHandler;
}
