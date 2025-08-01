package com.trtm.iot.swbhz.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BhzSwWarnVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预警总数")
    private Integer warnCount;
    @ApiModelProperty(value = "初级预警数")
    private Integer primaryCount;
    @ApiModelProperty(value = "中级预警数")
    private Integer middleCount;
    @ApiModelProperty(value = "高级预警数")
    private Integer advancedCount;
    @ApiModelProperty(value = "初级预警闭合数")
    private Integer primaryBiheCount;
    @ApiModelProperty(value = "中级预警闭合数")
    private Integer middleBiheCount;
    @ApiModelProperty(value = "高级预警闭合数")
    private Integer advancedBiheCount;


}
