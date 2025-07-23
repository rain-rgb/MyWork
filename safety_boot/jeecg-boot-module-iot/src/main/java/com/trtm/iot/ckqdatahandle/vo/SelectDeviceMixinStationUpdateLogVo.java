/**
 * @ClassName SelectDeviceMixinStationUpdateLogVo.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年08月02日 15:19:00
 */
package com.trtm.iot.ckqdatahandle.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class SelectDeviceMixinStationUpdateLogVo {

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    private String sid;

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


