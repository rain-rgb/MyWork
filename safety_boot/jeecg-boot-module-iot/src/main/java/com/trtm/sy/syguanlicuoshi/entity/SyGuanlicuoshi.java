package com.trtm.sy.syguanlicuoshi.entity;

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
 * @Description: sy_guanlicuoshi
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Data
@TableName("sy_guanlicuoshi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_guanlicuoshi对象", description="sy_guanlicuoshi")
public class SyGuanlicuoshi implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
    private String batchId;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    @Dict(dicCode = "nodeType")
    private String cailiaoType;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private String guigexinghao;
	/**料仓*/
	@Excel(name = "料仓", width = 15)
    @ApiModelProperty(value = "料仓")
    private String liaocang;
	/**设备号*/
	@Excel(name = "设备号", width = 15)
    @ApiModelProperty(value = "设备号")
    private String shebeiNo;
	/**指摘人*/
	@Excel(name = "指摘人", width = 15)
    @ApiModelProperty(value = "指摘人")
    private String zhizhairen;
	/**被指摘施工单位*/
	@Excel(name = "被指摘施工单位", width = 15)
    @ApiModelProperty(value = "被指摘施工单位")
    private String bzzSgDw;
	/**被指摘监理单位*/
	@Excel(name = "被指摘监理单位", width = 15)
    @ApiModelProperty(value = "被指摘监理单位")
    private String bzzJlDw;
	/**被指摘厂家*/
	@Excel(name = "被指摘厂家", width = 15)
    @ApiModelProperty(value = "被指摘厂家")
    private String bzzCj;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String comment;
	/**指摘时间*/
	@Excel(name = "指摘时间", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "指摘时间")
    private Date zzTime;
	/**指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）*/
	@Excel(name = "指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）", width = 15)
    @ApiModelProperty(value = "指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）")
    @Dict(dicCode = "zzType")
    private String zzType;
	/**指摘原因*/
	@Excel(name = "指摘原因", width = 15)
    @ApiModelProperty(value = "指摘原因")
    private String zzReason;
	/**检验委托单号*/
	@Excel(name = "检验委托单号", width = 15)
    @ApiModelProperty(value = "检验委托单号")
    private String testCommissionedId;
}
