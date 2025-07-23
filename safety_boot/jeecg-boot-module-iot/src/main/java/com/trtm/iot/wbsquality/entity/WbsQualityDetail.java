package com.trtm.iot.wbsquality.entity;

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
 * @Description: wbs_quality_detail
 * @Author: jeecg-boot
 * @Date:   2024-11-27
 * @Version: V1.0
 */
@Data
@TableName("wbs_quality_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wbs_quality_detail对象", description="wbs_quality_detail")
public class WbsQualityDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**wbs 结构类型*/
	@Excel(name = "wbs 结构类型", width = 15)
    @ApiModelProperty(value = "wbs 结构类型")
    private String wbsStructureType;
	/**模板id*/
	@Excel(name = "模板id", width = 15)
    @ApiModelProperty(value = "模板id")
    private String templateid;
	/**质检单编号*/
	@Excel(name = "质检单编号", width = 15)
    @ApiModelProperty(value = "质检单编号")
    private String reportNumber;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;
	/**reportName*/
	@Excel(name = "reportName", width = 15)
    @ApiModelProperty(value = "reportName")
    private String reportName;
	/**order*/
	@Excel(name = "order", width = 15)
    @ApiModelProperty(value = "order")
    private Integer orders;
	/**parentnote*/
	@Excel(name = "parentnote", width = 15)
    @ApiModelProperty(value = "parentnote")
    private String parentnote;
	/**部位id*/
	@Excel(name = "部位id", width = 15)
    @ApiModelProperty(value = "部位id")
    private String wbsid;
	/**树id*/
	@Excel(name = "树id", width = 15)
    @ApiModelProperty(value = "树id")
    private String treeid;
	/**0未填；10暂存；20已提交；30已审批*/
	@Excel(name = "0未填；10暂存；20已提交；30已审批", width = 15)
    @ApiModelProperty(value = "0未填；10暂存；20已提交；30已审批")
    private Integer status;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
}
