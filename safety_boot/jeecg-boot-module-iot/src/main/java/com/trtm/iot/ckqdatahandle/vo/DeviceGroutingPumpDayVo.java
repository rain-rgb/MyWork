/**
 * @ClassName DeviceGroutingPumpDayVo.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年08月03日 17:41:00
 */
package com.trtm.iot.ckqdatahandle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class DeviceGroutingPumpDayVo {

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    @ApiModelProperty(value = "注浆泵编号")
    private String groutingPumpCode;

    @ApiModelProperty(value = "时间")
    private String Time;

    @ApiModelProperty(value = "当日注浆总量")
    private Double todayGroutingTotal;

    @ApiModelProperty(value = "累计（所有天数加起来）")
    private Double allDayGroutingTotal;

    @ApiModelProperty(value = "标段号")
    private List<DeviceGroutingPumpDayDetailsVo> groutingPumpList;


}


