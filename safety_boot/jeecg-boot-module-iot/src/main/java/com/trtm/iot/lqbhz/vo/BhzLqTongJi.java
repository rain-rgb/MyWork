package com.trtm.iot.lqbhz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class BhzLqTongJi {

    @Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private Integer id;
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String shebeiName;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String cailiaoName;
    @Excel(name = "理论用量", width = 15)
    @ApiModelProperty(value = "理论用量")
    private String lilunUse;
    @Excel(name = "实际用量", width = 15)
    @ApiModelProperty(value = "实际用量")
    private String realUse;

}
