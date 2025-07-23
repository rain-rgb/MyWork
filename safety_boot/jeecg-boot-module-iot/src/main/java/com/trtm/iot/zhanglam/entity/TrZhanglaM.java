package com.trtm.iot.zhanglam.entity;

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
 * @Description: 张拉信息主表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla_m")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla_m对象", description="张拉信息主表")
public class TrZhanglaM implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String fguid;
	/**主键*/
	@Excel(name = "主键", width = 15)
    @ApiModelProperty(value = "主键")
    private java.lang.String syjid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")

    private java.lang.String shebeibianhao;
	/**张拉时间*/
	@Excel(name = "张拉时间", width = 15)
    @ApiModelProperty(value = "张拉时间")
    private java.lang.String zlsj;
	/**钢束编号*/
	@Excel(name = "钢束编号", width = 15)
    @ApiModelProperty(value = "钢束编号")
    private java.lang.String gsbh;
	/**holeid*/
	@Excel(name = "holeid", width = 15)
    @ApiModelProperty(value = "holeid")
    private java.lang.String holeid;
	/**钢束股数*/
	@Excel(name = "钢束股数", width = 15)
    @ApiModelProperty(value = "钢束股数")
    private java.lang.String gsgs;
	/**弹性模量*/
	@Excel(name = "弹性模量", width = 15)
    @ApiModelProperty(value = "弹性模量")
    private java.lang.String txml;
	/**设计张拉力 KN*/
	@Excel(name = "设计张拉力 KN", width = 15)
    @ApiModelProperty(value = "设计张拉力 KN")
    private java.lang.String sjzll;
	/**回弹量mm*/
	@Excel(name = "回弹量mm", width = 15)
    @ApiModelProperty(value = "回弹量mm")
    private java.lang.String htl;
	/**总伸长量mm*/
	@Excel(name = "总伸长量mm", width = 15)
    @ApiModelProperty(value = "总伸长量mm")
    private java.lang.String zscl;
	/**理论伸长量mm*/
	@Excel(name = "理论伸长量mm", width = 15)
    @ApiModelProperty(value = "理论伸长量mm")
    private java.lang.String llscl;
	/**允许偏差值*/
	@Excel(name = "允许偏差值", width = 15)
    @ApiModelProperty(value = "允许偏差值")
    private java.lang.String yxpc;
	/**延伸量误差%*/
	@Excel(name = "延伸量误差%", width = 15)
    @ApiModelProperty(value = "延伸量误差%")
    private java.lang.String sclper;
	/**断丝及处理情况*/
	@Excel(name = "断丝及处理情况", width = 15)
    @ApiModelProperty(value = "断丝及处理情况")
    private java.lang.String wt;
	/**处理情况*/
	@Excel(name = "处理情况", width = 15)
    @ApiModelProperty(value = "处理情况")
    private java.lang.String clqk;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String memo;
	/**完成状态(0为未完成,状态为1代表*/
	@Excel(name = "完成状态(0为未完成,状态为1代表", width = 15)
    @ApiModelProperty(value = "完成状态(0为未完成,状态为1代表")
    private java.lang.String status;
	/**是否合格*/
	@Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private java.lang.String hege;
	/**预张拉百分比*/
	@Excel(name = "预张拉百分比", width = 15)
    @ApiModelProperty(value = "预张拉百分比")
    private java.lang.String yzlb;
	/**初张拉百分比*/
	@Excel(name = "初张拉百分比", width = 15)
    @ApiModelProperty(value = "初张拉百分比")
    private java.lang.String czlb;
	/**终张拉百分比*/
	@Excel(name = "终张拉百分比", width = 15)
    @ApiModelProperty(value = "终张拉百分比")
    private java.lang.String zzlb;
	/**记录时间*/
	@Excel(name = "记录时间", width = 15)
    @ApiModelProperty(value = "记录时间")
    private java.lang.String jlsj;
    /**张拉任务单号*/
    @Excel(name = "张拉任务单号", width = 15)
    @ApiModelProperty(value = "张拉任务单号")
    private java.lang.String uuid;
    /**质检推送*/
    @Excel(name = "质检推送", width = 15)
    @ApiModelProperty(value = "质检推送")
    private java.lang.Integer issend;
    private java.lang.Integer overproofStatus;
    private String overReason;// 预警原因
    private Integer overLevel;//超标等级
    private Integer iszl;//超标等级

    private java.lang.Double scljs;
}
