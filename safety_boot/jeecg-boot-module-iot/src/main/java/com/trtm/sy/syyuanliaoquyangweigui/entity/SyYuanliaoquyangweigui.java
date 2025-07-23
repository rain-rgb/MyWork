package com.trtm.sy.syyuanliaoquyangweigui.entity;

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
 * @Description: sy_yuanliaoquyangweigui
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
@Data
@TableName("sy_yuanliaoquyangweigui")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_yuanliaoquyangweigui对象", description="sy_yuanliaoquyangweigui")
public class SyYuanliaoquyangweigui implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
    private java.lang.String batchId;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    private java.lang.String cailiaoType;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private java.lang.String guigexinghao;
	/**料仓*/
	@Excel(name = "料仓", width = 15)
    @ApiModelProperty(value = "料仓")
    private java.lang.String liaocang;
	/**设备号*/
	@Excel(name = "设备号", width = 15)
    @ApiModelProperty(value = "设备号")
    private java.lang.String shebeiNo;
	/**指摘人*/
	@Excel(name = "指摘人", width = 15)
    @ApiModelProperty(value = "指摘人")
    private java.lang.String zhizhairen;
	/**被指摘施工单位*/
	@Excel(name = "被指摘施工单位", width = 15)
    @ApiModelProperty(value = "被指摘施工单位")
    private java.lang.String bzzSgDw;
	/**被指摘监理单位*/
	@Excel(name = "被指摘监理单位", width = 15)
    @ApiModelProperty(value = "被指摘监理单位")
    private java.lang.String bzzJlDw;
	/**被指摘试验员*/
	@Excel(name = "被指摘试验员", width = 15)
    @ApiModelProperty(value = "被指摘试验员")
    private java.lang.String bzzSyy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String comment;
	/**指摘时间*/
	@Excel(name = "指摘时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "指摘时间")
    private java.util.Date zzTime;
	/**指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）*/
	@Excel(name = "指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）", width = 15)
    @ApiModelProperty(value = "指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）")
    @Dict(dicCode = "zzType")
    private java.lang.String zzType;
	/**指摘原因*/
	@Excel(name = "指摘原因", width = 15)
    @ApiModelProperty(value = "指摘原因")
    private java.lang.String zzReason;
	/**检验委托单号*/
	@Excel(name = "检验委托单号", width = 15)
    @ApiModelProperty(value = "检验委托单号")
    private java.lang.String testCommissionedId;
	/**取样图*/
	@Excel(name = "取样图", width = 15)
    @ApiModelProperty(value = "取样图")
    private java.lang.String quliaoPic;
	/**二维码*/
	@Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码")
    private java.lang.String qrCode;
	/**二维码图*/
	@Excel(name = "二维码图", width = 15)
    @ApiModelProperty(value = "二维码图")
    private java.lang.String qrCodeChart;
}
