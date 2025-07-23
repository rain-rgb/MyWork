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
 * @Description: sy_dps_sy_report_m
 * @Author: jeecg-boot
 * @Date:   2023-02-10
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_report_m")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_report_m对象", description="sy_dps_sy_report_m")
public class SyDpsSyReportM implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**主键UUID*/
	@Excel(name = "主键UUID", width = 15)
    @ApiModelProperty(value = "主键UUID")
    private String uuid;
	/**外键，样品编号（dps_sy_Sample）*/
	@Excel(name = "外键，样品编号（dps_sy_Sample）", width = 15)
    @ApiModelProperty(value = "外键，样品编号（dps_sy_Sample）")
    private String sampleno;
	/**报告编号(根据基础设置的规则生成)*/
	@Excel(name = "报告编号(根据基础设置的规则生成)", width = 15)
    @ApiModelProperty(value = "报告编号(根据基础设置的规则生成)")
    private String reportno;
	/**外键，组织机构id(T_S_depart)*/
	@Excel(name = "外键，组织机构id(T_S_depart)", width = 15)
    @ApiModelProperty(value = "外键，组织机构id(T_S_depart)")
    private String departid;
	/**外键，组织机构编码（T_S_depart）*/
	@Excel(name = "外键，组织机构编码（T_S_depart）", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码（T_S_depart）")
    private String orgcode;
	/**外键，分部分项编码（dps_jc_project）*/
	@Excel(name = "外键，分部分项编码（dps_jc_project）", width = 15)
    @ApiModelProperty(value = "外键，分部分项编码（dps_jc_project）")
    private String projectcode;
	/**报告创建日期(yyyy-MM-dd)*/
	@Excel(name = "报告创建日期(yyyy-MM-dd)", width = 15)
    @ApiModelProperty(value = "报告创建日期(yyyy-MM-dd)")
    private String reportcreatedate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String reportremark;
	/**报告编辑人*/
	@Excel(name = "报告编辑人", width = 15)
    @ApiModelProperty(value = "报告编辑人")
    private String reporteditperson;
	/**试验项目记录编号(根据基础设置的规则生成)*/
	@Excel(name = "试验项目记录编号(根据基础设置的规则生成)", width = 15)
    @ApiModelProperty(value = "试验项目记录编号(根据基础设置的规则生成)")
    private String tablenumber;
	/**分部分项名称*/
	@Excel(name = "分部分项名称", width = 15)
    @ApiModelProperty(value = "分部分项名称")
    private String projectname;
}
