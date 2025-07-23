/**
 * @ClassName CementFlyAsh.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月21日 16:23:00
 */
package com.trtm.iot.ckqdatahandle.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CementFlyAsh {

    @ApiModelProperty(value = "实际-水泥")
    private Double realityCement;

    @ApiModelProperty(value = "实际-粉煤灰")
    private Double realityFlyAsh;
}


