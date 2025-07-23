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
 * @Description: sy_dps_sy_report_s
 * @Author: jeecg-boot
 * @Date:   2023-02-10
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_report_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_report_s对象", description="sy_dps_sy_report_s")
public class SyDpsSyReportS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**主键UUID*/
	@Excel(name = "主键UUID", width = 15)
    @ApiModelProperty(value = "主键UUID")
    private String uuid;
	/**外键，试验项目编号/试验报告编号(dps_jc_testItem)*/
	@Excel(name = "外键，试验项目编号/试验报告编号(dps_jc_testItem)", width = 15)
    @ApiModelProperty(value = "外键，试验项目编号/试验报告编号(dps_jc_testItem)")
    private String tino;
	/**外键，报告编号（dps_sy_report_M）*/
	@Excel(name = "外键，报告编号（dps_sy_report_M）", width = 15)
    @ApiModelProperty(value = "外键，报告编号（dps_sy_report_M）")
    private String reportno;
	/**外键，样品编号（dps_sy_sample）*/
	@Excel(name = "外键，样品编号（dps_sy_sample）", width = 15)
    @ApiModelProperty(value = "外键，样品编号（dps_sy_sample）")
    private String sampleno;
	/**外键，试验项目类型（dps_jc_testItem）*/
	@Excel(name = "外键，试验项目类型（dps_jc_testItem）", width = 15)
    @ApiModelProperty(value = "外键，试验项目类型（dps_jc_testItem）")
    private Integer titype;
	/**编辑状态（0：未编辑 1：已编辑）默认0*/
	@Excel(name = "编辑状态（0：未编辑 1：已编辑）默认0", width = 15)
    @ApiModelProperty(value = "编辑状态（0：未编辑 1：已编辑）默认0")
    private Integer editstate;
	/**临时试验项目编号（如果记录表出现一次则为1）*/
	@Excel(name = "临时试验项目编号（如果记录表出现一次则为1）", width = 15)
    @ApiModelProperty(value = "临时试验项目编号（如果记录表出现一次则为1）")
    private Integer tinotemp;
}
