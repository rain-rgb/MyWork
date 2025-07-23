package com.trtm.sy.sylxdps.entity;

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
 * @Description: sy_dps_jc_testitem
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_jc_testitem")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_jc_testitem对象", description="sy_dps_jc_testitem")
public class SyDpsJcTestitem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;
	/**主键UUID*/
	@Excel(name = "主键UUID", width = 15)
    @ApiModelProperty(value = "主键UUID")
    private String uuid;
	/**外键（dps_jc_testItemType）试验项目类型*/
	@Excel(name = "外键（dps_jc_testItemType）试验项目类型", width = 15)
    @ApiModelProperty(value = "外键（dps_jc_testItemType）试验项目类型")
    private String titcode;
	/**外键（dps_jc_rules）规程编号*/
	@Excel(name = "外键（dps_jc_rules）规程编号", width = 15)
    @ApiModelProperty(value = "外键（dps_jc_rules）规程编号")
    private String ruleno;
	/**试验项目编号/试验报告编号*/
	@Excel(name = "试验项目编号/试验报告编号", width = 15)
    @ApiModelProperty(value = "试验项目编号/试验报告编号")
    private String tino;
	/**试验项目名称*/
	@Excel(name = "试验项目名称", width = 15)
    @ApiModelProperty(value = "试验项目名称")
    private String tiname;
	/**试验项目参数号*/
	@Excel(name = "试验项目参数号", width = 15)
    @ApiModelProperty(value = "试验项目参数号")
    private String tiparameternum;
	/**试验项目表号*/
	@Excel(name = "试验项目表号", width = 15)
    @ApiModelProperty(value = "试验项目表号")
    private String titablenum;
	/**试验项目表格名称*/
	@Excel(name = "试验项目表格名称", width = 15)
    @ApiModelProperty(value = "试验项目表格名称")
    private String titablename;
	/**试验项目默认组数*/
	@Excel(name = "试验项目默认组数", width = 15)
    @ApiModelProperty(value = "试验项目默认组数")
    private Integer tidefaultgroupnum;
	/**试验项目排序号*/
	@Excel(name = "试验项目排序号", width = 15)
    @ApiModelProperty(value = "试验项目排序号")
    private Integer tisort;
	/**试验项目备注*/
	@Excel(name = "试验项目备注", width = 15)
    @ApiModelProperty(value = "试验项目备注")
    private String tiremark;
	/**ticustomtablenum*/
	@Excel(name = "ticustomtablenum", width = 15)
    @ApiModelProperty(value = "ticustomtablenum")
    private String ticustomtablenum;
	/**类型（0：试验项目1：报告2：评定）*/
	@Excel(name = "类型（0：试验项目1：报告2：评定）", width = 15)
    @ApiModelProperty(value = "类型（0：试验项目1：报告2：评定）")
    private Integer titype;
	/**是否删除（0：正常1：已删除）*/
	@Excel(name = "是否删除（0：正常1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：正常1：已删除）")
    @Dict(dicCode = "tiisdel")
    private Integer tiisdel;
	/**tijishuzhibiao*/
	@Excel(name = "tijishuzhibiao", width = 15)
    @ApiModelProperty(value = "tijishuzhibiao")
    private String tijishuzhibiao;
	/**tizhongyaozhibiao*/
	@Excel(name = "tizhongyaozhibiao", width = 15)
    @ApiModelProperty(value = "tizhongyaozhibiao")
    private String tizhongyaozhibiao;
	/**tizhongyaozhibiaoname*/
	@Excel(name = "tizhongyaozhibiaoname", width = 15)
    @ApiModelProperty(value = "tizhongyaozhibiaoname")
    private String tizhongyaozhibiaoname;
	/**tibaogaotaizhangname*/
	@Excel(name = "tibaogaotaizhangname", width = 15)
    @ApiModelProperty(value = "tibaogaotaizhangname")
    private String tibaogaotaizhangname;
	/**tibaogaotaizhangno*/
	@Excel(name = "tibaogaotaizhangno", width = 15)
    @ApiModelProperty(value = "tibaogaotaizhangno")
    private String tibaogaotaizhangno;
	/**plusmark*/
	@Excel(name = "plusmark", width = 15)
    @ApiModelProperty(value = "plusmark")
    private Integer plusmark;
	/**tip*/
	@Excel(name = "tip", width = 15)
    @ApiModelProperty(value = "tip")
    private String tip;
	/**tipandingyiju*/
	@Excel(name = "tipandingyiju", width = 15)
    @ApiModelProperty(value = "tipandingyiju")
    private String tipandingyiju;
	/**tishiyanyiju*/
	@Excel(name = "tishiyanyiju", width = 15)
    @ApiModelProperty(value = "tishiyanyiju")
    private String tishiyanyiju;
}
