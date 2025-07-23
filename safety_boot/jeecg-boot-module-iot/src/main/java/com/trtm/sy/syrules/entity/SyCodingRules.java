package com.trtm.sy.syrules.entity;

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
 * @Description: sy_coding_rules
 * @Author: jeecg-boot
 * @Date:   2023-06-29
 * @Version: V1.0
 */
@Data
@TableName("sy_coding_rules")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_coding_rules对象", description="sy_coding_rules")
public class SyCodingRules implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键（自增长）*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键（自增长）")
    private Integer id;
	/**部门表id*/
	@Excel(name = "部门表id", width = 15)
    @ApiModelProperty(value = "部门表id")
    private String departId;
	/**委托单编号规则*/
	@Excel(name = "委托单编号规则", width = 15)
    @ApiModelProperty(value = "委托单编号规则")
    private String weituoTableCodingRules;
	/**取样的编号规则*/
	@Excel(name = "取样的编号规则", width = 15)
    @ApiModelProperty(value = "取样的编号规则")
    private String quyangTableCodingRules;
	/**任务单编号规则*/
	@Excel(name = "任务单编号规则", width = 15)
    @ApiModelProperty(value = "任务单编号规则")
    private String renwuTableCodingRules;
	/**报验单编码规则，跟着样品流水*/
	@Excel(name = "报验单编码规则，跟着样品流水", width = 15)
    @ApiModelProperty(value = "报验单编码规则，跟着样品流水")
    private String reportingSheetCodingRules;
	/**审批表编码规则，跟着样品流水*/
	@Excel(name = "审批表编码规则，跟着样品流水", width = 15)
    @ApiModelProperty(value = "审批表编码规则，跟着样品流水")
    private String approvalTableCodingRules;
	/**评定表编码规则*/
	@Excel(name = "评定表编码规则", width = 15)
    @ApiModelProperty(value = "评定表编码规则")
    private String evaluationTableCodingRules;
	/**samplecodingrules*/
	@Excel(name = "samplecodingrules", width = 15)
    @ApiModelProperty(value = "samplecodingrules")
    private String sampleCodingRules;
	/**reportcodingrules*/
	@Excel(name = "reportcodingrules", width = 15)
    @ApiModelProperty(value = "reportcodingrules")
    private String reportCodingRules;
	/**recordcodingrules*/
	@Excel(name = "recordcodingrules", width = 15)
    @ApiModelProperty(value = "recordcodingrules")
    private String recordCodingRules;
	/**codingdateformat*/
	@Excel(name = "codingdateformat", width = 15, format = "yyyy-MM-dd")
//	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "codingdateformat")
    private String codingDateFormat;
	/**codingflownumerdigit*/
	@Excel(name = "codingflownumerdigit", width = 15)
    @ApiModelProperty(value = "codingflownumerdigit")
    private Integer codingFlowNumerDigit;
    private String contractNumber;
    private String contractNumberCode;

}
