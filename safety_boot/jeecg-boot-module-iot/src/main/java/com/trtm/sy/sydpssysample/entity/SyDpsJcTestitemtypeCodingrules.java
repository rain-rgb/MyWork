package com.trtm.sy.sydpssysample.entity;

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
 * @Description: sy_dps_jc_testitemtype_codingrules
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_jc_testitemtype_codingrules")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_jc_testitemtype_codingrules对象", description="sy_dps_jc_testitemtype_codingrules")
public class SyDpsJcTestitemtypeCodingrules implements Serializable {
    private static final long serialVersionUID = 1L;

	/**外键，组织机构id,来源T_S_Depart表id*/
	@Excel(name = "外键，组织机构id,来源T_S_Depart表id", width = 15)
    @ApiModelProperty(value = "外键，组织机构id,来源T_S_Depart表id")
    private String departid;
	/**样品编码规则*/
	@Excel(name = "样品编码规则", width = 15)
    @ApiModelProperty(value = "样品编码规则")
    private String samplecodingrules;
	/**报告编码规则*/
	@Excel(name = "报告编码规则", width = 15)
    @ApiModelProperty(value = "报告编码规则")
    private String reportcodingrules;
	/**记录编码规则*/
	@Excel(name = "记录编码规则", width = 15)
    @ApiModelProperty(value = "记录编码规则")
    private String recordcodingrules;
	/**编码时间格式,来源于数据字典*/
	@Excel(name = "编码时间格式,来源于数据字典", width = 15)
    @ApiModelProperty(value = "编码时间格式,来源于数据字典")
    private String codingdateformat;
	/**编码流水号位数，例如4位那么就是从0001开始*/
	@Excel(name = "编码流水号位数，例如4位那么就是从0001开始", width = 15)
    @ApiModelProperty(value = "编码流水号位数，例如4位那么就是从0001开始")
    private Integer codingflownumerdigit;
	/**报验单编码规则，跟着样品流水*/
	@Excel(name = "报验单编码规则，跟着样品流水", width = 15)
    @ApiModelProperty(value = "报验单编码规则，跟着样品流水")
    private String reportingsheetcodingrules;
	/**审批表编码规则，跟着样品流水*/
	@Excel(name = "审批表编码规则，跟着样品流水", width = 15)
    @ApiModelProperty(value = "审批表编码规则，跟着样品流水")
    private String approvaltablecodingrules;
	/**外键，试验类型编码，来源dps_jc_testItemType#试验项目类型表*/
	@Excel(name = "外键，试验类型编码，来源dps_jc_testItemType#试验项目类型表", width = 15)
    @ApiModelProperty(value = "外键，试验类型编码，来源dps_jc_testItemType#试验项目类型表")
    private String titcode;
	/**evaluationtablecodingrules*/
	@Excel(name = "evaluationtablecodingrules", width = 15)
    @ApiModelProperty(value = "evaluationtablecodingrules")
    private String evaluationtablecodingrules;
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
}
