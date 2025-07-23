package com.trtm.iot.rebarComponentTask.entity;

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
 * @Description: rebar_component_task
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Data
@TableName("rebar_component_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rebar_component_task对象", description="rebar_component_task")
public class RebarComponentTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**构件主键id*/
	@Excel(name = "构件主键id", width = 15)
    @ApiModelProperty(value = "构件主键id")
    private java.lang.String componentId;
	/**任务清单主键id*/
	@Excel(name = "任务清单主键id", width = 15)
    @ApiModelProperty(value = "任务清单主键id")
    private java.lang.String taskId;
//	/**设计构件数量*/
//	@Excel(name = "设计构件数量", width = 15)
//    @ApiModelProperty(value = "设计构件数量")
//    private java.lang.String designComponentNumber;
	/**领用构件数量*/
	@Excel(name = "领用构件数量", width = 15)
    @ApiModelProperty(value = "领用构件数量")
    private java.lang.String componentNumber;
    /**部门机构编码*/
    @Excel(name = "部门机构编码", width = 15)
    @ApiModelProperty(value = "部门机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCode;
    /**分部分项组织机构编码*/
    @Excel(name = "分部分项组织机构编码", width = 15)
    @ApiModelProperty(value = "分部分项组织机构编码")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String orgCodes;
}
