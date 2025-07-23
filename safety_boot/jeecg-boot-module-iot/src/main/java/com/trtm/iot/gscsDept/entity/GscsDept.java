package com.trtm.iot.gscsDept.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: 班组安全管控系统班组表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
@Data
@TableName("gscs_dept")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "gscs_dept对象", description = "班组安全管控系统班组表")
public class GscsDept implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
    /**
     * 班组id
     */
    @Excel(name = "班组id", width = 15)
    @ApiModelProperty(value = "班组id")
    private java.lang.String deptId;
    /**
     * 班组名
     */
    @Excel(name = "班组名", width = 15)
    @ApiModelProperty(value = "班组名")
    private java.lang.String deptName;
    /**
     * 状态（0启用，1禁用，默认0）
     */
    @Excel(name = "状态（0启用，1禁用，默认0）", width = 15)
    @ApiModelProperty(value = "状态（0启用，1禁用，默认0）")
    private java.lang.Integer status;
    /**
     * 逻辑删除（0正常，1删除，默认0）
     */
    @Excel(name = "逻辑删除（0正常，1删除，默认0）", width = 15)
    @ApiModelProperty(value = "逻辑删除（0正常，1删除，默认0）")
    private java.lang.Integer delFlag;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
