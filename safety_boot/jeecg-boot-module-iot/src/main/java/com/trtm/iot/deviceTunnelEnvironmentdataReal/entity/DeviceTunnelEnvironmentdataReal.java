package com.trtm.iot.deviceTunnelEnvironmentdataReal.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: device_tunnel_environmentdata_real
 * @Author: jeecg-boot
 * @Date:   2022-08-18
 * @Version: V1.0
 */
@Data
@TableName("device_tunnel_environmentdata_real")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_tunnel_environmentdata_real对象", description="device_tunnel_environmentdata_real")
public class DeviceTunnelEnvironmentdataReal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**地点*/
	@Excel(name = "地点", width = 15)
    @ApiModelProperty(value = "地点")
    private java.lang.String site;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeino;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date datatime;
	/**0无报警，1低报警，2高报警*/
	@Excel(name = "0无报警，1低报警，2高报警", width = 15)
    @ApiModelProperty(value = "0无报警，1低报警，2高报警")
    private java.lang.Integer alarmtype;
	/**氧气*/
	@Excel(name = "氧气", width = 15)
    @ApiModelProperty(value = "氧气")
    private java.lang.String oxygen;
	/**甲烷*/
	@Excel(name = "甲烷", width = 15)
    @ApiModelProperty(value = "甲烷")
    private java.lang.String methane;
	/**硫化氢*/
	@Excel(name = "硫化氢", width = 15)
    @ApiModelProperty(value = "硫化氢")
    private java.lang.String hSulfide;
	/**二氧化碳*/
	@Excel(name = "二氧化碳", width = 15)
    @ApiModelProperty(value = "二氧化碳")
    private java.lang.String cDioxide;
	/**一氧化碳*/
	@Excel(name = "一氧化碳", width = 15)
    @ApiModelProperty(value = "一氧化碳")
    private java.lang.String cMonoxide;
	/**风速*/
	@Excel(name = "风速", width = 15)
    @ApiModelProperty(value = "风速")
    private java.lang.String windspeed;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15)
    @ApiModelProperty(value = "数据时间")
    private java.lang.String uploadtime;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.String temperature;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
	/**粉尘*/
	@Excel(name = "粉尘", width = 15)
    @ApiModelProperty(value = "粉尘")
    private java.lang.String dust;
	/**噪声*/
	@Excel(name = "噪声", width = 15)
    @ApiModelProperty(value = "噪声")
    private java.lang.String noise;
	/**PM1.0*/
	@Excel(name = "PM1.0", width = 15)
    @TableField("PM1")
    @ApiModelProperty(value = "PM1.0")
    private java.lang.String pm10;
	/**PM2.0*/
	@Excel(name = "PM2.0", width = 15)
    @TableField("PM2")
    @ApiModelProperty(value = "PM2.0")
    private java.lang.String pm20;
	/**PM10*/
	@Excel(name = "PM10", width = 15)
    @TableField("PM10")
    @ApiModelProperty(value = "PM10")
    private java.lang.String pm10s;
}
