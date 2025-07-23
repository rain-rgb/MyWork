package com.trtm.iot.zhilianggongxu.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: 制梁工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-22
 * @Version: V1.0
 */
@Data
@TableName("zhiliang_gongxu")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zhiliang_gongxu对象", description="制梁工序表信息")
public class ZhiliangGongxu implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**唯一值*/
	@Excel(name = "唯一值", width = 15)
    @ApiModelProperty(value = "唯一值")
    @Dict(dictTable = "zhiliangrenwudan", dicText = "Code", dicCode = "uuid")
    private java.lang.String uuid;
	/**工序序号(工序字典值)*/
	@Excel(name = "工序序号(工序字典值)", width = 15)
    @ApiModelProperty(value = "工序序号(工序字典值)")
    @Dict(dicCode = "xuhao")
    private java.lang.Integer xuhao;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**责任人*/
	@Excel(name = "责任人", width = 15)
    @ApiModelProperty(value = "责任人")
    private java.lang.String responsible;
	/**完成时间*/
    @TableField(updateStrategy= FieldStrategy.IGNORED)
	@Excel(name = "完成时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完成时间")
    private java.util.Date finishtime;

    /**0 未完成  1 进行中   2已完成*/
	@Excel(name = "0 未完成  1 进行中   2已完成", width = 15)
    @ApiModelProperty(value = "0 未完成  1 进行中   2已完成")
    @Dict(dicCode = "status1")
    private java.lang.Integer status;
    /**梁所存层*/
    @Excel(name = "梁所存层", width = 15)
    @ApiModelProperty(value = "梁所存层")
    private java.lang.Integer liangceng;
    /**存梁行*/
    @Excel(name = "存梁行", width = 15)
    @ApiModelProperty(value = "存梁行")
    private java.lang.String cunlianghang;
    /**存梁列*/
    @Excel(name = "存梁列", width = 15)
    @ApiModelProperty(value = "存梁列")
    private java.lang.String cunlianglie;
    /**梁场设备编号*/
    @Excel(name = "梁场设备编号", width = 15)
    @ApiModelProperty(value = "梁场设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
    /**存入梁时间*/
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    @Excel(name = "存入梁时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "存入梁时间")
    private java.util.Date cuntime;
    /**存入梁监管人*/
    @Excel(name = "存入梁监管人", width = 15)
    @ApiModelProperty(value = "存入梁监管人")
    private java.lang.String cunpeople;
    /**出场时间*/
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    @Excel(name = "出场时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出场时间")
    private java.util.Date chutime;
    /**出场监管人*/
    @Excel(name = "出场监管人", width = 15)
    @ApiModelProperty(value = "出场监管人")
    private java.lang.String chupeople;
    /**梁场台座名称*/
    @Excel(name = "梁场台座名称", width = 15)
    @ApiModelProperty(value = "梁场台座名称")
    private java.lang.String taizuoname;
    @Excel(name = "存梁状态 1 已存入 2已出场", width = 15)
    @ApiModelProperty(value = "存梁状态 1 已存入 2已出场")
    private java.lang.Integer status1;
    private java.lang.String remark;
    private java.lang.Integer isdel;
    private java.lang.String cluuid;
    @Dict(dicCode = "unit")
    private java.lang.Integer unit;
    private java.lang.Integer pinmin;

    private java.lang.String picurl;
}
