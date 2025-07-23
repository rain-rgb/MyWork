package com.trtm.iot.taskTransfer.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 任务单转移
 * @Author: jeecg-boot
 * @Date:   2022-11-23
 * @Version: V1.0
 */
@Data
@TableName("task_transfer")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="任务单转移", description="任务单转移")
public class TaskTransfer implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;

	/**任务单编号*/
	@Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private java.lang.String taskCode;

	/**原任务单编号*/
	@Excel(name = "原任务单编号", width = 15)
    @ApiModelProperty(value = "原任务单编号")
    private java.lang.String originalCode;

	/**方量*/
	@Excel(name = "方量", width = 15)
    @ApiModelProperty(value = "方量")
    private java.lang.Double mate;

	/**操作时间*/
	@Excel(name = "操作时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "操作时间")
    private java.util.Date handleTime;

	/**操作者*/
	@Excel(name = "操作者", width = 15)
    @ApiModelProperty(value = "操作者")
    private java.lang.String handler;

	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private java.lang.Integer type;

	/**转入类型*/
	@Excel(name = "转入类型", width = 15)
    @ApiModelProperty(value = "转入类型")
    private java.lang.Integer intoType;

	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

	/**逻辑删除（0：存在，1：已删除，默认为0）*/
	@Excel(name = "逻辑删除（0：存在，1：已删除，默认为0）", width = 15)
    @ApiModelProperty(value = "逻辑删除（0：存在，1：已删除，默认为0）")
    private java.lang.Integer deleteFlag;

    @Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "修改方式")
    @TableField("sys_org_code")
    private String sysOrgCode;
}
