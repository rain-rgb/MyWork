package com.trtm.iot.trzhanglastatistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class ZlstatisticsVO {
    /**本月超标总条数*/
    @Excel(name = "本月超标总条数", width = 15)
    @ApiModelProperty(value = "本月超标总条数")
    private int monthOverSum;
    /**超标总条数*/
    @Excel(name = "超标总条数", width = 15)
    @ApiModelProperty(value = "超标总条数")
    private int Oversum;
}
