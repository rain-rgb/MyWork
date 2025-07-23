package com.trtm.iot.ckqdatahandle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DeviceGroutingPumpVo {

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    @ApiModelProperty(value = "注浆泵名称")
    private String groutingPumpName;

    @ApiModelProperty(value = "注浆泵编号")
    private String groutingPumpCode;

    @ApiModelProperty(value = "施工井号")
    private String  constructionWellCode;

    @ApiModelProperty(value = "当前页码",required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "每页显示条数",required = true)
    private Integer pageSize;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
