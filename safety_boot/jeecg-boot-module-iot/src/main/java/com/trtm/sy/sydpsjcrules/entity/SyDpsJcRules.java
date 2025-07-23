package com.trtm.sy.sydpsjcrules.entity;

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
 * @Description: sy_dps_jc_rules
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_jc_rules")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_jc_rules对象", description="sy_dps_jc_rules")
public class SyDpsJcRules implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
    /**uuid*/
    @Excel(name = "uuid", width = 15)
    @ApiModelProperty(value = "uuid")
    private String uuid;
	/**titcode*/
	@Excel(name = "titcode", width = 15)
    @ApiModelProperty(value = "titcode")
    private String titcode;
	/**规程编号*/
	@Excel(name = "规程编号", width = 15)
    @ApiModelProperty(value = "规程编号")
    private String ruleno;
	/**规程名称*/
	@Excel(name = "规程名称", width = 15)
    @ApiModelProperty(value = "规程名称")
    private String rulename;
	/**规程类型*/
	@Excel(name = "规程类型", width = 15)
    @ApiModelProperty(value = "规程类型")
    @Dict(dicCode = "ruleType")
    private String ruletype;
	/**规程单位*/
	@Excel(name = "规程单位", width = 15)
    @ApiModelProperty(value = "规程单位")
    private String ruleunit;
	/**规程实施时间*/
	@Excel(name = "规程实施时间", width = 15)
    @ApiModelProperty(value = "规程实施时间")
    private String rulebegindate;
	/**显示状态*/
	@Excel(name = "显示状态", width = 15)
    @ApiModelProperty(value = "显示状态")
    private Integer ruleisshow;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String ruleremark;
	/**删除状态(0:正常 1:已删除)*/
	@Excel(name = "删除状态(0:正常 1:已删除)", width = 15)
    @ApiModelProperty(value = "删除状态(0:正常 1:已删除)")
    @Dict(dicCode = "ruleisdel")
    private Integer ruleisdel;
}
