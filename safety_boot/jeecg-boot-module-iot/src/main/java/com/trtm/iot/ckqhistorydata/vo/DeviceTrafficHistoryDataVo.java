/**
 * @ClassName DeviceTrafficHistoryDataVo.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月22日 12:06:00
 */
package com.trtm.iot.ckqhistorydata.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class DeviceTrafficHistoryDataVo {

    @ApiModelProperty(value = "设备编号", required = true)
    private String sid;

}


