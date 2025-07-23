package com.trtm.iot.wztaizhangweituodan.entity;

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
 * @Description: 原材料检验委托申请表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Data
@TableName("wztaizhangweituodan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wztaizhangweituodan对象", description="原材料检验委托申请表信息")
public class Wztaizhangweituodan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**1:粗骨料2：粉煤灰3：水泥4：细骨料*/
	@Excel(name = "1:粗骨料 2：粉煤灰 3：水泥 4：细骨料 ", width = 15)
    @ApiModelProperty(value = "1:粗骨料 2：粉煤灰 3：水泥 4：细骨料 ")
    @Dict(dicCode="nodeType")
    private java.lang.String state;
	/**报表*/
	@Excel(name = "报表", width = 15)
    @ApiModelProperty(value = "报表")
    private java.lang.String biaohao;
	/**标准代号*/
	@Excel(name = "标准代号", width = 15)
    @ApiModelProperty(value = "标准代号")
    private java.lang.String biaozhundaihao;
	/**委托单位*/
	@Excel(name = "委托单位", width = 15)
    @ApiModelProperty(value = "委托单位")
    private java.lang.String weituodanwei;
	/**委托编号*/
	@Excel(name = "委托编号", width = 15)
    @ApiModelProperty(value = "委托编号")
    private java.lang.String weituobianhao;
	/**样品名称*/
	@Excel(name = "样品名称", width = 15)
    @ApiModelProperty(value = "样品名称")
    private java.lang.String yangpingname;
	/**送样编号*/
	@Excel(name = "送样编号", width = 15)
    @ApiModelProperty(value = "送样编号")
    private java.lang.String songyangno;
	/**产地厂名*/
	@Excel(name = "产地厂名", width = 15)
    @ApiModelProperty(value = "产地厂名")
    private java.lang.String chandichangming;
	/**试样数量*/
	@Excel(name = "试样数量", width = 15)
    @ApiModelProperty(value = "试样数量")
    private java.lang.String shiyangnum;
	/**规格种类*/
	@Excel(name = "规格种类", width = 15)
    @ApiModelProperty(value = "规格种类")
    private java.lang.String guigezhonglei;
	/**取样地点*/
	@Excel(name = "取样地点", width = 15)
    @ApiModelProperty(value = "取样地点")
    private java.lang.String quyangdidian;
	/**代表数量(吨)*/
	@Excel(name = "代表数量(吨)", width = 15)
    @ApiModelProperty(value = "代表数量(吨)")
    private java.lang.String daibiaonum;
	/**样品描述*/
	@Excel(name = "样品描述", width = 15)
    @ApiModelProperty(value = "样品描述")
    private java.lang.String yangpingmiaoshu;
	/**取样日期*/
	@Excel(name = "取样日期", width = 15)
    @ApiModelProperty(value = "取样日期")
    private java.lang.String quyangtime;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String gongchengmingchen;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String shigongbuwei;
	/**检验标准*/
	@Excel(name = "检验标准", width = 15)
    @ApiModelProperty(value = "检验标准")
    private java.lang.String jianyanbiaozhun;
	/**试验项目*/
	@Excel(name = "试验项目", width = 15)
    @ApiModelProperty(value = "试验项目")
    private java.lang.String shiyanxiangmu;
	/**收样日期*/
	@Excel(name = "收样日期", width = 15)
    @ApiModelProperty(value = "收样日期")
    private java.lang.String shouyangtime;
	/**试样验收意见*/
	@Excel(name = "试样验收意见", width = 15)
    @ApiModelProperty(value = "试样验收意见")
    private java.lang.String yanshouyijian;
	/**取样人*/
	@Excel(name = "取样人", width = 15)
    @ApiModelProperty(value = "取样人")
    private java.lang.String quyangren;
	/**取样见证人*/
	@Excel(name = "取样见证人", width = 15)
    @ApiModelProperty(value = "取样见证人")
    private java.lang.String quyangjianzhengren;
	/**委托人*/
	@Excel(name = "委托人", width = 15)
    @ApiModelProperty(value = "委托人")
    private java.lang.String weituoren;
	/**委托单位负责人*/
	@Excel(name = "委托单位负责人", width = 15)
    @ApiModelProperty(value = "委托单位负责人")
    private java.lang.String weituodanweifuzeren;
	/**住址及邮编*/
	@Excel(name = "住址及邮编", width = 15)
    @ApiModelProperty(value = "住址及邮编")
    private java.lang.String zhuzhaijiyoubian;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String lianxiphone;
	/**收样人*/
	@Excel(name = "收样人", width = 15)
    @ApiModelProperty(value = "收样人")
    private java.lang.String shouyangren;
	/**预计报告发出日期*/
	@Excel(name = "预计报告发出日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计报告发出日期")
    private java.util.Date yujifachutime;
	/**tzid*/
	@Excel(name = "tzid", width = 15)
    @ApiModelProperty(value = "tzid")
    private java.lang.Integer tzid;
	/**moduleid*/
	@Excel(name = "moduleid", width = 15)
    @ApiModelProperty(value = "moduleid")
    private java.lang.String moduleid;
	/**modulename*/
	@Excel(name = "modulename", width = 15)
    @ApiModelProperty(value = "modulename")
    private java.lang.String modulename;
	/**是否录入*/
	@Excel(name = "是否录入", width = 15)
    @ApiModelProperty(value = "是否录入")
    private java.lang.String shifouluru;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.String isdel;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    @Dict(dictTable = "wzcailiaonamedict", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private java.lang.String pici;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeibianhao;
	/**净重（吨）*/
	@Excel(name = "净重（吨）", width = 15)
    @ApiModelProperty(value = "净重（吨）")
    private java.lang.String jingzhongt;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
	/**收样id*/
	@Excel(name = "收样id", width = 15)
    @ApiModelProperty(value = "收样id")
    private java.lang.String shouyangid;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createtime;

    @Dict(dicCode="qydw")
    private java.lang.String qydw;
    private java.lang.Integer wttype;
    private java.lang.String qrcode;//(二维码，多项以逗号隔开，最后一个二维码不用加逗号)
    private java.lang.Integer wtlc;//委托流程（0：未取样；1：已取样；2已收样；3试验中；4试验完成）


}

