package com.trtm.iot.september.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: GPS历史轨迹
 * @Author: jeecg-boot
 * @Date:   2022-04-01
 * @Version: V1.0
 */
@Data
@TableName("front_device_history_september")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="front_device_history_september对象", description="GPS历史轨迹")
public class September {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
	private java.lang.String imei;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
	private java.lang.String lng;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
	private java.lang.String lat;
	/**速度*/
	@Excel(name = "速度", width = 15)
    @ApiModelProperty(value = "速度")
	private java.lang.Integer speed;
	/**航向*/
	@Excel(name = "航向", width = 15)
    @ApiModelProperty(value = "航向")
	private java.lang.Integer course;
	/**GPS时间(时间戳)*/
	@Excel(name = "GPS时间(时间戳)", width = 15)
    @ApiModelProperty(value = "GPS时间(时间戳)")
	private java.lang.Integer gpstime;
	/**定位类型*/
	@Excel(name = "定位类型", width = 15)
    @ApiModelProperty(value = "定位类型")
	private java.lang.String positiontype;
}
