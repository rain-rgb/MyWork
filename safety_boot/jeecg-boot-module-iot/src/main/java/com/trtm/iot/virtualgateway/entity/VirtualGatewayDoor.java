package com.trtm.iot.virtualgateway.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: virtual_gateway_door
 * @Author: jeecg-boot
 * @Date:   2023-07-07
 * @Version: V1.0
 */
@Data
@TableName("virtual_gateway_door")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="virtual_gateway_door对象", description="virtual_gateway_door")
public class VirtualGatewayDoor implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private java.lang.String deviceId;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15)
    @ApiModelProperty(value = "采集时间")
    private java.lang.String collectionTime;
	/**仓门状态:1:开门; 0:关门.*/
	@Excel(name = "仓门状态:1:开门; 0:关门.", width = 15)
    @ApiModelProperty(value = "仓门状态:1:开门; 0:关门.")
    private java.lang.String door;
	/**闯入报警状态：0：没有报警，1：报警*/
	@Excel(name = "闯入报警状态：0：没有报警，1：报警", width = 15)
    @ApiModelProperty(value = "闯入报警状态：0：没有报警，1：报警")
    private java.lang.String sos;
}
