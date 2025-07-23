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
 * @Description: sy_dps_sy_sample_view2
 * @Author: jeecg-boot
 * @Date:   2023-01-30
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_sample_view2")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_sample_view2对象", description="sy_dps_sy_sample_view2")
public class SyDpsSySampleView2 implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键UUID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键UUID")
    private String id;
	/**样品编号(根据基础配置的编号规则生成)*/
	@Excel(name = "样品编号(根据基础配置的编号规则生成)", width = 15)
    @ApiModelProperty(value = "样品编号(根据基础配置的编号规则生成)")
    private String sampleno;
	/**样品名称*/
	@Excel(name = "样品名称", width = 15)
    @ApiModelProperty(value = "样品名称")
    private String samplename;
	/**样品描述*/
	@Excel(name = "样品描述", width = 15)
    @ApiModelProperty(value = "样品描述")
    private String sampledescribe;
	/**工程部位/用途*/
	@Excel(name = "工程部位/用途", width = 15)
    @ApiModelProperty(value = "工程部位/用途")
    private String samplegcbw;
	/**取样日期（yyyy-MM-dd）*/
	@Excel(name = "取样日期（yyyy-MM-dd）", width = 15)
    @ApiModelProperty(value = "取样日期（yyyy-MM-dd）")
    private String sampledate;
	/**取样地点/取样位置*/
	@Excel(name = "取样地点/取样位置", width = 15)
    @ApiModelProperty(value = "取样地点/取样位置")
    private String samplequyangdidian;
	/**样品状态（0：待检1：在检2：已检）*/
	@Excel(name = "样品状态（0：待检1：在检2：已检）", width = 15)
    @ApiModelProperty(value = "样品状态（0：待检1：在检2：已检）")
    private Integer samplestate;
	/**真实名字*/
	@Excel(name = "真实名字", width = 15)
    @ApiModelProperty(value = "真实名字")
    private String realname;
	/**取样人*/
	@Excel(name = "取样人", width = 15)
    @ApiModelProperty(value = "取样人")
    private String samplequyangren;
	/**创建人（系统登录用户）*/
	@Excel(name = "创建人（系统登录用户）", width = 15)
    @ApiModelProperty(value = "创建人（系统登录用户）")
    private String samplecreateperson;
	/**创建日期（yyyy-MM-dd）*/
	@Excel(name = "创建日期（yyyy-MM-dd）", width = 15)
    @ApiModelProperty(value = "创建日期（yyyy-MM-dd）")
    private String samplecreatedate;
	/**外键，组织机构编码（T_S_depart）*/
	@Excel(name = "外键，组织机构编码（T_S_depart）", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码（T_S_depart）")
    private String orgcode;
	/**外键，试验项目类型（dps_jc_testItemType表）*/
	@Excel(name = "外键，试验项目类型（dps_jc_testItemType表）", width = 15)
    @ApiModelProperty(value = "外键，试验项目类型（dps_jc_testItemType表）")
    private String titcode;
	/**强度等级，使用数据字典（水泥样品用到）*/
	@Excel(name = "强度等级，使用数据字典（水泥样品用到）", width = 15)
    @ApiModelProperty(value = "强度等级，使用数据字典（水泥样品用到）")
    private String sampleqiangdudengji;
	/**龄期 */
	@Excel(name = "龄期 ", width = 15)
    @ApiModelProperty(value = "龄期 ")
    private String samplelingqi;
	/**报告日期（来源编辑报告时填写的时间）*/
	@Excel(name = "报告日期（来源编辑报告时填写的时间）", width = 15)
    @ApiModelProperty(value = "报告日期（来源编辑报告时填写的时间）")
    private String reportcreatedate;
	/**报告编号(根据基础设置的规则生成)*/
	@Excel(name = "报告编号(根据基础设置的规则生成)", width = 15)
    @ApiModelProperty(value = "报告编号(根据基础设置的规则生成)")
    private String reportno;
	/**报告编辑人*/
	@Excel(name = "报告编辑人", width = 15)
    @ApiModelProperty(value = "报告编辑人")
    private String reporteditperson;
	/**真实名字*/
	@Excel(name = "真实名字", width = 15)
    @ApiModelProperty(value = "真实名字")
    private String realname2;
	/**试验项目名称*/
	@Excel(name = "试验项目名称", width = 15)
    @ApiModelProperty(value = "试验项目名称")
    private String titname;
	/**外键（dps_jc_testItemType）试验项目类型*/
	@Excel(name = "外键（dps_jc_testItemType）试验项目类型", width = 15)
    @ApiModelProperty(value = "外键（dps_jc_testItemType）试验项目类型")
    private String tittype;
	/**testname*/
	@Excel(name = "testname", width = 15)
    @ApiModelProperty(value = "testname")
    private String testname;
	/**见证取样 0：普通 1：见证取样 默认给0*/
	@Excel(name = "见证取样 0：普通 1：见证取样 默认给0", width = 15)
    @ApiModelProperty(value = "见证取样 0：普通 1：见证取样 默认给0")
    private Integer plusmark;
	/**样品编号(根据基础配置的编号规则生成)*/
	@Excel(name = "样品编号(根据基础配置的编号规则生成)", width = 15)
    @ApiModelProperty(value = "样品编号(根据基础配置的编号规则生成)")
    private String samplenonew;
	/**自定义报告编号（雷榕项目）*/
	@Excel(name = "自定义报告编号（雷榕项目）", width = 15)
    @ApiModelProperty(value = "自定义报告编号（雷榕项目）")
    private String reportnonew;
	/**自定义记录编号（雷榕项目）*/
	@Excel(name = "自定义记录编号（雷榕项目）", width = 15)
    @ApiModelProperty(value = "自定义记录编号（雷榕项目）")
    private String tablenumbernew;
	/**审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回*/
	@Excel(name = "审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回", width = 15)
    @ApiModelProperty(value = "审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回")
    private Integer shenpizhuangtai;
}
