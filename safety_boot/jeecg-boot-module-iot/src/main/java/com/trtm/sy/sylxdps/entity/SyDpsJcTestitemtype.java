package com.trtm.sy.sylxdps.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: sy_dps_jc_testitemtype
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_jc_testitemtype")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_jc_testitemtype对象", description="sy_dps_jc_testitemtype")
public class SyDpsJcTestitemtype implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;
	/**主键UUID*/
	@Excel(name = "主键UUID", width = 15)
    @ApiModelProperty(value = "主键UUID")
    private String uuid;
	/**试验类型编码(两位编码01开始)*/
	@Excel(name = "试验类型编码(两位编码01开始)", width = 15)
    @ApiModelProperty(value = "试验类型编码(两位编码01开始)")
    private String titcode;
	/**试验类型名称*/
	@Excel(name = "试验类型名称", width = 15)
    @ApiModelProperty(value = "试验类型名称")
    private String titname;
	/**样品标识*/
	@Excel(name = "样品标识", width = 15)
    @ApiModelProperty(value = "样品标识")
    private String titsamplemark;
	/**试验类型父节点编码*/
	@Excel(name = "试验类型父节点编码", width = 15)
    @ApiModelProperty(value = "试验类型父节点编码")
    private String titparentcode;
	/**当前节点的子节点个数*/
	@Excel(name = "当前节点的子节点个数", width = 15)
    @ApiModelProperty(value = "当前节点的子节点个数")
    private String titchildnodesnum;
	/**试验类型备注*/
	@Excel(name = "试验类型备注", width = 15)
    @ApiModelProperty(value = "试验类型备注")
    private String titremark;
	/**是否删除（0：正常1：已删除）*/
	@Excel(name = "是否删除（0：正常1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：正常1：已删除）")
    @Dict(dicCode = "titisdel")
    private Integer titisdel;
	/**tipandingyiju*/
	@Excel(name = "tipandingyiju", width = 15)
    @ApiModelProperty(value = "tipandingyiju")
    private String tipandingyiju;
	/**tishiyanyiju*/
	@Excel(name = "tishiyanyiju", width = 15)
    @ApiModelProperty(value = "tishiyanyiju")
    private String tishiyanyiju;
	/**tittype*/
	@Excel(name = "tittype", width = 15)
    @ApiModelProperty(value = "tittype")
    @Dict(dicCode = "titType")
    private Integer tittype;
	/**记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe*/
	@Excel(name = "记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe", width = 15)
    @ApiModelProperty(value = "记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe")
    private String tiyangpinxinxijl;
	/**报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容*/
	@Excel(name = "报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容", width = 15)
    @ApiModelProperty(value = "报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容")
    private String tiyangpinxinxibg;
	/**tipushtablename*/
	@Excel(name = "tipushtablename", width = 15)
    @ApiModelProperty(value = "tipushtablename")
    private String tipushtablename;

    @TableField(exist = false)
    private List<SyDpsJcTestitemtype> children;
}
