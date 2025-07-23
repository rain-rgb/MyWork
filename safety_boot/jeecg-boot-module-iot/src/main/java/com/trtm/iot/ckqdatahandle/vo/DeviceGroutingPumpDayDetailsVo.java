/**
 * @ClassName DeviceGroutingPumpDayDetailsVo.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年08月03日 17:44:00
 */
package com.trtm.iot.ckqdatahandle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DeviceGroutingPumpDayDetailsVo {

    @ApiModelProperty(value = "注浆泵编号")
    private String groutingPumpCode;

    @ApiModelProperty(value = "注浆总量")
    private Double groutingTotal;

}


