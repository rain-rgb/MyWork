package com.trtm.iot.lqbhz.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BhzLqWarnVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预警总数")
    private Integer warnCount;
    @ApiModelProperty(value = "初级预警数")
    private Integer primaryCount;
    @ApiModelProperty(value = "中级预警数")
    private Integer middleCount;
    @ApiModelProperty(value = "高级预警数")
    private Integer advancedCount;
    @ApiModelProperty(value = "拌合时间超标数")
    private Integer timeCount;
    @ApiModelProperty(value = "拌合时间超标闭合数")
    private Integer timeBiheCount;
    @ApiModelProperty(value = "初级预警闭合数")
    private Integer primaryBiheCount;
    @ApiModelProperty(value = "中级预警闭合数")
    private Integer middleBiheCount;
    @ApiModelProperty(value = "高级预警闭合数")
    private Integer advancedBiheCount;
    @ApiModelProperty(value = "沥青温度预警数")
    private Integer wenduWarnCount;
    @ApiModelProperty(value = "沥青温度预警闭合数")
    private Integer wenduBiheCount;
    @ApiModelProperty(value = "骨料温度预警数")
    private Integer guliaoWarnCount;
    @ApiModelProperty(value = "骨料温度预警闭合数")
    private Integer guliaoBiheCount;
    @ApiModelProperty(value = "出料温度预警数")
    private Integer chuliaoWarnCount;
    @ApiModelProperty(value = "出料温度预警闭合数")
    private Integer chuliaoBiheCount;
    @ApiModelProperty(value = "油石比预警数")
    private Integer ysbWarnCount;
    @ApiModelProperty(value = "油石比预警闭合数")
    private Integer ysbBiheCount;


}
