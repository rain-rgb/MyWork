package com.trtm.iot.hntbhz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BhzCementBaseCbTongji {
    private String name ;
    @ApiModelProperty(value = "总数")
    private Integer allCount;
    @ApiModelProperty(value = "初级预警数")
    private Integer primaryCount;
    @ApiModelProperty(value = "中级预警数")
    private Integer middleCount;
    @ApiModelProperty(value = "高级预警数")
    private Integer advancedCount;
    private Double advancedWarnLv;//高级预警率
    private Double primaryWarnLv;//初级预警率
    private Double middleWarnLv;//中级预警率
    private Double advancedHandleLv;//
    private Double primaryHandleLv;//
    private Double middleHandleLv;//
    private Integer advancedHandleCount;//
    private Integer primaryHandleCount;//
    private Integer middleHandleCount;//
    private Double zongchangliang;
    private String shebeiNo;
    private String orgCode;
}
