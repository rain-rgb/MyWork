package com.trtm.iot.skip_car.entity;

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
 * @Description: 运输温度
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
@Data
@TableName("skip_car")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="skip_car对象", description="运输温度")
public class SkipCar implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String deviceid;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.String temperature;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15)
    @ApiModelProperty(value = "采集时间")
    private java.lang.String collectiontime;
	/**航向*/
	@Excel(name = "航向", width = 15)
    @ApiModelProperty(value = "航向")
    private java.lang.String hangxiang;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String lon;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String lat;
	/**高程*/
	@Excel(name = "高程", width = 15)
    @ApiModelProperty(value = "高程")
    private java.lang.String gaocheng;
	/**速度*/
	@Excel(name = "速度", width = 15)
    @ApiModelProperty(value = "速度")
    private java.lang.Double speed;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String devicename;
}
