package com.trtm.iot.car.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;


/**
 * @author lis1
 * @date 2022/11/15
 * @time 15:35
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "运输车详情返回对象", description = "car_dispatch")
public class GetCarDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "运输车列表")
    private List<SchedulingCar> carAndMixingStationList;

    @ApiModelProperty(value = "车次数", dataType = "Integer")
    private Integer carQuantity;

    @ApiModelProperty(value = "超标数", dataType = "Integer")
    private Integer overProof;

    @ApiModelProperty(value = "闭合数", dataType = "Integer")
    private Integer close;

    @ApiModelProperty(value = "浇筑时长", dataType = "Integer")
    private String castingTime;

    @ApiModelProperty(value = "运输超时", dataType = "Integer")
    private Integer transportTimeout;
}
