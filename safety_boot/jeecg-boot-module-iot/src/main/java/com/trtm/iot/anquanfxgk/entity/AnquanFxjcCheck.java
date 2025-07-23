package com.trtm.iot.anquanfxgk.entity;

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
 * @Description: anquan_fxjc_check
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxjc_check")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxjc_check对象", description="anquan_fxjc_check")
public class AnquanFxjcCheck implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**年度如 2024年度研判*/
	@Excel(name = "年度如 2024年度研判", width = 15)
    @ApiModelProperty(value = "年度如 2024年度研判")
    private java.lang.String niandu;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**所属业务部门*/
	@Excel(name = "所属业务部门", width = 15)
    @ApiModelProperty(value = "所属业务部门")
    private java.lang.String departname;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String note;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**风险源点责任部*/
	@Excel(name = "风险源点责任部", width = 15)
    @ApiModelProperty(value = "风险源点责任部")
    private java.lang.String riskDepart;
	/**风险源点名称*/
	@Excel(name = "风险源点名称", width = 15)
    @ApiModelProperty(value = "风险源点名称")
    private java.lang.String riskName;
	/**问题描述*/
	@Excel(name = "问题描述", width = 15)
    @ApiModelProperty(value = "问题描述")
    private java.lang.String problems;
	/**风险等级*/
	@Excel(name = "风险等级", width = 15)
    @ApiModelProperty(value = "风险等级")
    private java.lang.String riskLevel;
	/**检查人*/
	@Excel(name = "检查人", width = 15)
    @ApiModelProperty(value = "检查人")
    private java.lang.String checkperson;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private java.lang.String serialNo;
	/**信息反馈*/
	@Excel(name = "信息反馈", width = 15)
    @ApiModelProperty(value = "信息反馈")
    private java.lang.String infoBack;
}
