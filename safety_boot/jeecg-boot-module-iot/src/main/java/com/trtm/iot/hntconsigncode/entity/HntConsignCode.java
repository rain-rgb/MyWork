package com.trtm.iot.hntconsigncode.entity;

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
 * @Description: 混凝土见证取样二维码表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Data
@TableName("hnt_consign_code")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hnt_consign_code对象", description="混凝土见证取样二维码表信息")
public class HntConsignCode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**二维码*/
	@Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码")
    private java.lang.String codeno;
	/**委托ID--guid*/
	@Excel(name = "委托ID--guid", width = 15)
    @ApiModelProperty(value = "委托ID--guid")
    private java.lang.String wtid;
	/**委托编号*/
	@Excel(name = "委托编号", width = 15)
    @ApiModelProperty(value = "委托编号")
    private java.lang.String wtbh;
	/**状态（0：取样，1：开始养护，2：出养护室）*/
	@Excel(name = "状态（0：取样，1：开始养护，2：出养护室）", width = 15)
    @ApiModelProperty(value = "状态（0：取样，1：开始养护，2：出养护室）")
    @Dict(dicCode = "cstatus")
    private java.lang.Integer cstatus;
	/**养护时间*/
	@Excel(name = "养护时间", width = 15)
    @ApiModelProperty(value = "养护时间")
    private java.lang.String jyhdate;
	/**进养护人*/
	@Excel(name = "进养护人", width = 15)
    @ApiModelProperty(value = "进养护人")
    private java.lang.String jyhr;
	/**出养护室时间*/
	@Excel(name = "出养护室时间", width = 15)
    @ApiModelProperty(value = "出养护室时间")
    private java.lang.String cyhdate;
	/**出养护人*/
	@Excel(name = "出养护人", width = 15)
    @ApiModelProperty(value = "出养护人")
    private java.lang.String cyhr;
	/**取样人*/
	@Excel(name = "取样人", width = 15)
    @ApiModelProperty(value = "取样人")
    private java.lang.String qyr;
	/**取样时间*/
	@Excel(name = "取样时间", width = 15)
    @ApiModelProperty(value = "取样时间")
    private java.lang.String qydate;
	/**外键，与t_consigin_sample_pos关联*/
	@Excel(name = "外键，与t_consigin_sample_pos关联", width = 15)
    @ApiModelProperty(value = "外键，与t_consigin_sample_pos关联")
    private java.lang.String huojianname;
	/**货物所在层数*/
	@Excel(name = "货物所在层数", width = 15)
    @ApiModelProperty(value = "货物所在层数")
    private java.lang.String huojiacenshu;
	/**二维码处理字段*/
	@Excel(name = "二维码处理字段", width = 15)
    @ApiModelProperty(value = "二维码处理字段")
    private java.lang.String code;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.String no;
}
