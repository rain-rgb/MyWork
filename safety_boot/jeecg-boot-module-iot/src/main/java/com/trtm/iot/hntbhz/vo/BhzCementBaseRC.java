package com.trtm.iot.hntbhz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BhzCementBaseRC implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer allDish;
    @ApiModelProperty(value = "初级预警数")
    private Integer primaryCount;
    @ApiModelProperty(value = "中级预警数")
    private Integer middleCount;
    @ApiModelProperty(value = "高级预警数")
    private Integer advancedCount;
    private Integer allNotHandleDish;
    private Integer allHandleDish;
    private Integer primaryNotHandle;
    private Integer middleNotHandle;
    private Integer advanceNotHandle;

}
