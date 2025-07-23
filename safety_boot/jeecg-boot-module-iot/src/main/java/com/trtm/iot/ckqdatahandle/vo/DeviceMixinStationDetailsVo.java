package com.trtm.iot.ckqdatahandle.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceMixinStationDetailsVo {

    @ApiModelProperty("材料名称")
    private String materialName;

    @ApiModelProperty(value = "理论值")
    private Double theoryCement;

    @ApiModelProperty(value = "实际值")
    private Double realityCement;

    @ApiModelProperty(value = "误差")
    private Double errorValues;

    @ApiModelProperty(value = "误差率")
    private Double errorRate;

}
