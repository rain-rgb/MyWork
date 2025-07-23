package com.trtm.iot.devicehistory.entity;

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
 * @Description: 环境监测历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
@Data
@TableName("devicehistory")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="devicehistory对象", description="环境监测历史记录")
public class Devicehistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**设备地址*/
	@Excel(name = "设备地址", width = 15)
    @ApiModelProperty(value = "设备地址")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String devaddr;
	/**唯一标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.Integer id;
	/**PM10(ug/m³)（最高200）*/
	@Excel(name = "PM10(ug/m³)（最高200）", width = 15)
    @ApiModelProperty(value = "PM10(ug/m³)（最高200）")
    private java.lang.String pm10;
	/**PM2.5(ug/m³)（最高500）*/
	@Excel(name = "PM2.5(ug/m³)（最高500）", width = 15)
    @ApiModelProperty(value = "PM2.5(ug/m³)（最高500）")
    private java.lang.String pm25;
	/**噪声(dB)（最高不高于100）*/
	@Excel(name = "噪声(dB)（最高不高于100）", width = 15)
    @ApiModelProperty(value = "噪声(dB)（最高不高于100）")
    private java.lang.String noise;
	/**温度(℃)*/
	@Excel(name = "温度(℃)", width = 15)
    @ApiModelProperty(value = "温度(℃)")
    private java.lang.String temperature;
	/**湿度(%RH)（最高100）*/
	@Excel(name = "湿度(%RH)（最高100）", width = 15)
    @ApiModelProperty(value = "湿度(%RH)（最高100）")
    private java.lang.String humidity;
	/**风力（最高18）*/
	@Excel(name = "风力（最高18）", width = 15)
    @ApiModelProperty(value = "风力（最高18）")
    private java.lang.String wind;
	/**风速(m/s) （最高不大于100）*/
	@Excel(name = "风速(m/s) （最高不大于100）", width = 15)
    @ApiModelProperty(value = "风速(m/s) （最高不大于100）")
    private java.lang.String windspeed;
	/**风向  （风向度最高360）*/
	@Excel(name = "风向  （风向度最高360）", width = 15)
    @ApiModelProperty(value = "风向  （风向度最高360）")
    private java.lang.String winddirection;
	/**数据保存的时间点*/
	@Excel(name = "数据保存的时间点", width = 15, format = "yyyy-MM-dd HH-mm-ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据保存的时间点")
    private java.util.Date timevalue;
	private java.lang.Integer alertstate;//是否超标判断
    private java.lang.Integer status;//状态(0:优 1:良 2:差)
    private java.lang.Integer pm25status;//pm2.5 状态(0;优 1:良 2:差)
    private java.lang.Integer pm10status;//pm10 状态(0;优 1:良 2:差)
    private java.lang.Integer noisestatus;//噪音 状态(0;优 1:良 2:差)
    private java.lang.Integer temstatus;//温度状态(0 正常 1 异常)
    private java.lang.Integer humstatus;//湿度状态(0 正常 1 异常)
    private java.lang.Integer windstatus;//风力状态(0 正常 1 异常)
    private java.lang.Integer winsdstatus;//风速状态(0 正常 1 异常)
    private java.lang.String tsp;//tsp(pm100)
    private java.lang.String atmpressure;//大气压
    private Integer overproofStatus;//审核状态
    private Integer istuisong;//苏台推送
    private java.lang.String overproofInfo;//预警原因
}
