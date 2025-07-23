package com.trtm.iot.taskUpdate.entity;

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
 * @Description: 任务单修改记录
 * @Author: lis1
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Data
@TableName("task_update")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="任务单修改记录对象", description="任务单修改记录")
public class TaskUpdate implements Serializable {
    private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id，自增")
    private java.lang.Integer id;

	@Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private java.lang.String taskCode;

    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

	@Excel(name = "修改方式", width = 15)
    @ApiModelProperty(value = "修改方式")
    private java.lang.String updateWay;

    @Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "修改方式")
    @TableField("sys_org_code")
    private String sysOrgCode;
}
