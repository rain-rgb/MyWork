package com.trtm.iot.ckqdatahandle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data

public class SelectDeviceMixinStationVo {

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    @ApiModelProperty(value = "搅拌站号")
    private String mixinStationCode;

    @ApiModelProperty(value = "当前页码", required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
