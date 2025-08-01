package com.trtm.iot.hntconsign.entity;

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
 * @Description: 混凝土见证取样表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Data
@TableName("hnt_consign")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hnt_consign对象", description="混凝土见证取样表信息")
public class HntConsign implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**委托ID--guid*/
	@Excel(name = "委托ID--guid", width = 15)
    @ApiModelProperty(value = "委托ID--guid")
    private java.lang.String wtid;
	/**委托编号*/
	@Excel(name = "委托编号", width = 15)
    @ApiModelProperty(value = "委托编号")
    private java.lang.String wtbh;
	/**样品名称*/
	@Excel(name = "样品名称", width = 15)
    @ApiModelProperty(value = "样品名称")
    private java.lang.String ypmc;
	/**产地厂名（生产厂家）*/
	@Excel(name = "产地厂名（生产厂家）", width = 15)
    @ApiModelProperty(value = "产地厂名（生产厂家）")
    private java.lang.String cdcm;
	/**代表数量*/
	@Excel(name = "代表数量", width = 15)
    @ApiModelProperty(value = "代表数量")
    private java.lang.String sysl;
	/**取样地点\取样位置*/
	@Excel(name = "取样地点（取样位置）", width = 15)
    @ApiModelProperty(value = "取样地点（取样位置）")
    private java.lang.String qydd;
	/**样品描述*/
	@Excel(name = "样品描述", width = 15)
    @ApiModelProperty(value = "样品描述")
    private java.lang.String ypms;
	/**取样日期*/
	@Excel(name = "取样日期", width = 15)
    @ApiModelProperty(value = "取样日期")
    private java.lang.String qyrq;
	/**取样人*/
	@Excel(name = "取样人", width = 15)
    @ApiModelProperty(value = "取样人")
    private java.lang.String qyr;
	/**配比通知单ID*/
	@Excel(name = "配比通知单ID", width = 15)
    @ApiModelProperty(value = "配比通知单ID")
    private java.lang.String phbtzdid;
	/**配比通知单编号*/
	@Excel(name = "配比通知单编号", width = 15)
    @ApiModelProperty(value = "配比通知单编号")
    private java.lang.String phbtzdbh;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String gcmc;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String sgbw;
	/**试验类型（1：压力试验，2：万能实验）*/
	@Excel(name = "试验类型（1：压力试验，2：万能实验）", width = 15)
    @ApiModelProperty(value = "试验类型（1：压力试验，2：万能实验）")
    @Dict(dicCode = "testtype")
    private java.lang.Integer testtype;
	/**试验项目编号*/
	@Excel(name = "试验项目编号", width = 15)
    @ApiModelProperty(value = "试验项目编号")
    private java.lang.String syxm;
	/**试验项目名称*/
	@Excel(name = "试验项目名称", width = 15)
    @ApiModelProperty(value = "试验项目名称")
    private java.lang.String syxmmc;
	/**样品试验组数。默认值（水泥1，水泥混凝土1，钢筋1）*/
	@Excel(name = "样品试验组数。默认值（水泥1，水泥混凝土1，钢筋1）", width = 15)
    @ApiModelProperty(value = "样品试验组数。默认值（水泥1，水泥混凝土1，钢筋1）")
    private java.lang.String sysuliang;
	/**强度等级（混凝土）*/
	@Excel(name = "强度等级（混凝土）", width = 15)
    @ApiModelProperty(value = "强度等级（混凝土）")
    private java.lang.String qddj;
	/**混凝土种类（混凝土）*/
	@Excel(name = "混凝土种类（混凝土）", width = 15)
    @ApiModelProperty(value = "混凝土种类（混凝土）")
    private java.lang.String hntzl;
	/**搅拌方式（混凝土）*/
	@Excel(name = "搅拌方式（混凝土）", width = 15)
    @ApiModelProperty(value = "搅拌方式（混凝土）")
    private java.lang.String jbfs;
	/**样品龄期（混凝土）*/
	@Excel(name = "样品龄期（混凝土）", width = 15)
    @ApiModelProperty(value = "样品龄期（混凝土）")
    private java.lang.String yplq;
	/**样品备注*/
	@Excel(name = "样品备注", width = 15)
    @ApiModelProperty(value = "样品备注")
    private java.lang.String sybz;
	/**砂浆种类*/
	@Excel(name = "砂浆种类", width = 15)
    @ApiModelProperty(value = "砂浆种类")
    private java.lang.String sjzl;
	/**钢筋直径*/
	@Excel(name = "钢筋直径", width = 15)
    @ApiModelProperty(value = "钢筋直径")
    private java.lang.String gjzj;
	/**钢筋种类*/
	@Excel(name = "钢筋种类", width = 15)
    @ApiModelProperty(value = "钢筋种类")
    private java.lang.String gjzl;
	/**委托单位*/
	@Excel(name = "委托单位", width = 15)
    @ApiModelProperty(value = "委托单位")
    private java.lang.String wtdw;
	/**试验编号*/
	@Excel(name = "试验编号", width = 15)
    @ApiModelProperty(value = "试验编号")
    private java.lang.String sybh;
	/**试验单位*/
	@Excel(name = "试验单位", width = 15)
    @ApiModelProperty(value = "试验单位")
    private java.lang.String sydw;
	/**规格种类*/
	@Excel(name = "规格种类", width = 15)
    @ApiModelProperty(value = "规格种类")
    private java.lang.String ggzl;
	/**单位数量*/
	@Excel(name = "单位数量", width = 15)
    @ApiModelProperty(value = "单位数量")
    private java.lang.String ddsl;
	/**检验标准*/
	@Excel(name = "检验标准", width = 15)
    @ApiModelProperty(value = "检验标准")
    private java.lang.String jybz;
	/**ysyj*/
	@Excel(name = "ysyj", width = 15)
    @ApiModelProperty(value = "ysyj")
    private java.lang.String ysyj;
	/**试验日期*/
	@Excel(name = "试验日期", width = 15)
    @ApiModelProperty(value = "试验日期")
    private java.lang.String syrq;
	/**取证见证人*/
	@Excel(name = "取证见证人", width = 15)
    @ApiModelProperty(value = "取证见证人")
    private java.lang.String qyjzr;
	/**委托人*/
	@Excel(name = "委托人", width = 15)
    @ApiModelProperty(value = "委托人")
    private java.lang.String wtr;
	/**委托单位负责人*/
	@Excel(name = "委托单位负责人", width = 15)
    @ApiModelProperty(value = "委托单位负责人")
    private java.lang.String wtfzr;
	/**地址及邮件*/
	@Excel(name = "地址及邮件", width = 15)
    @ApiModelProperty(value = "地址及邮件")
    private java.lang.String zzyb;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String lldh;
	/**收样人*/
	@Excel(name = "收样人", width = 15)
    @ApiModelProperty(value = "收样人")
    private java.lang.String syr;
	/**预计报告发出日期*/
	@Excel(name = "预计报告发出日期", width = 15)
    @ApiModelProperty(value = "预计报告发出日期")
    private java.lang.String yjbgrq;
	/**sfsyj*/
	@Excel(name = "sfsyj", width = 15)
    @ApiModelProperty(value = "sfsyj")
    private java.lang.Integer sfsyj;
	/**现场是否完成试验0：未完成1：已完成 默认给0*/
	@Excel(name = "现场是否完成试验0：未完成1：已完成 默认给0", width = 15)
    @ApiModelProperty(value = "现场是否完成试验0：未完成1：已完成 默认给0")
    @Dict(dicCode = "mstatus")
    private java.lang.Integer mstatus;
	/**外键，组织机构id*/
	@Excel(name = "外键，组织机构id", width = 15)
    @ApiModelProperty(value = "外键，组织机构id")
    private java.lang.String departid;
	/**外键，组织机构编码*/
	@Excel(name = "外键，组织机构编码", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgcode;
	/**二维码，多项以逗号隔开，最后一个二维码不用加逗号*/
	@Excel(name = "二维码，多项以逗号隔开，最后一个二维码不用加逗号", width = 15)
    @ApiModelProperty(value = "二维码，多项以逗号隔开，最后一个二维码不用加逗号")
    private java.lang.String qrcode;
	/**samplejidu*/
	@Excel(name = "samplejidu", width = 15)
    @ApiModelProperty(value = "samplejidu")
    private java.lang.String samplejidu;
	/**sampleweidu*/
	@Excel(name = "sampleweidu", width = 15)
    @ApiModelProperty(value = "sampleweidu")
    private java.lang.String sampleweidu;
	/**ts*/
	@Excel(name = "ts", width = 15)
    @ApiModelProperty(value = "ts")
    private java.lang.String ts;
	/**试验尺寸*/
	@Excel(name = "试验尺寸", width = 15)
    @ApiModelProperty(value = "试验尺寸")
    private java.lang.String chicun;
	/**判断试件的状态开始时间*/
	@Excel(name = "判断试件的状态开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "判断试件的状态开始时间")
    private java.util.Date starttime;
	/**判断试件的状态结束时间*/
	@Excel(name = "判断试件的状态结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "判断试件的状态结束时间")
    private java.util.Date endtime;

    private String projectid;
    private String sectionid;

}
