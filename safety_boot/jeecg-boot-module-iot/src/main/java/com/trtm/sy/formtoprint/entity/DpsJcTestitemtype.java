package com.trtm.sy.formtoprint.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 表单打印
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
@Data
@TableName("dps_jc_testitemtype")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dps_jc_testitemtype对象", description="表单打印")
public class DpsJcTestitemtype implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键UUID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键UUID")
    private String id;

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

    //试验类型树形列表查询字段：id
    private String resultId;

    //试验类型树形列表查询字段：name
    private String resultName;

    //试验类型树形列表查询字段：isParent
    private String isParent;

    //试验类型树形列表查询字段：parentId
    private String parentId;
}
