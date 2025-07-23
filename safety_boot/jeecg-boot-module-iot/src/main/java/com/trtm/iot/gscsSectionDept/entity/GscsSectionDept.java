package com.trtm.iot.gscsSectionDept.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("gscs_section_dept")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "gscs_section_dept对象", description = "班组安全管控系统标段与班组关联表")
public class GscsSectionDept implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Integer id;
    /**
     * 标段
     */
    @Excel(name = "标段", width = 15)
    @ApiModelProperty(value = "标段")
    private String section;
    /**
     * 班组id
     */
    @Excel(name = "班组id", width = 15)
    @ApiModelProperty(value = "班组id")
    private String deptId;
    /**
     * 状态（0启用，1禁用，默认0）
     */
    @Excel(name = "状态（0启用，1禁用，默认0）", width = 15)
    @ApiModelProperty(value = "状态（0启用，1禁用，默认0）")
    private Integer status;
    /**
     * 逻辑删除（0正常，1删除，默认0）
     */
    @Excel(name = "逻辑删除（0正常，1删除，默认0）", width = 15)
    @ApiModelProperty(value = "逻辑删除（0正常，1删除，默认0）")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
