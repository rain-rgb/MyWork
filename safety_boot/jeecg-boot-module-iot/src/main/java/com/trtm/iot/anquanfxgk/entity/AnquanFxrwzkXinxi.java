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
 * @Description: anquan_fxrwzk_xinxi
 * @Author: jeecg-boot
 * @Date:   2024-08-08
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxrwzk_xinxi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxrwzk_xinxi对象", description="anquan_fxrwzk_xinxi")
public class AnquanFxrwzkXinxi implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**主表guid*/
//	@Excel(name = "主表guid", width = 15)
    @ApiModelProperty(value = "主表guid")
    private java.lang.String baseGuid;
	/**唯一键*/
//	@Excel(name = "唯一键", width = 15)
    @ApiModelProperty(value = "唯一键")
    private java.lang.String baseid;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**工点名称*/
	@Excel(name = "工点名称", width = 15)
    @ApiModelProperty(value = "工点名称")
    private java.lang.String workPointsName;
	/**主要内容*/
	@Excel(name = "主要内容", width = 15)
    @ApiModelProperty(value = "主要内容")
    private java.lang.String primaryCoverage;
	/**风险等级*/
	@Excel(name = "风险等级", width = 15)
    @ApiModelProperty(value = "风险等级")
    private java.lang.String riskLevel;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private java.lang.String construction;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private java.lang.String supervisionUnit;
	/**主要风险因素*/
	@Excel(name = "主要风险因素", width = 15)
    @ApiModelProperty(value = "主要风险因素")
    private java.lang.String mainRiskFactor;
	/**对策措施*/
	@Excel(name = "对策措施", width = 15)
    @ApiModelProperty(value = "对策措施")
    private java.lang.String counterMeasures;
	/**管控-建设单位*/
	@Excel(name = "管控-建设单位", width = 15,groupName = "管控责任人")
    @ApiModelProperty(value = "建设单位")
    private java.lang.String managDevelopment;
	/**管控-施工单位*/
	@Excel(name = "管控-施工单位", width = 15,groupName = "管控责任人")
    @ApiModelProperty(value = "施工单位")
    private java.lang.String managConstruction;
	/**管控-监理单位*/
	@Excel(name = "管控-监理单位", width = 15,groupName = "管控责任人")
    @ApiModelProperty(value = "监理单位")
    private java.lang.String managSupervision;
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
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
    @ApiModelProperty(value = "是否整改（0未整改1整改2驳回）")
    private java.lang.String isRectify;
}
