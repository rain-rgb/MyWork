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
 * @Description: devicehistory_wether
 * @Author: jeecg-boot
 * @Date:   2024-07-29
 * @Version: V1.0
 */
@Data
@TableName("devicehistory_wether")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="devicehistory_wether对象", description="devicehistory_wether")
public class DevicehistoryWether implements Serializable {
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
	/**雷达水位计空高(cm)*/
	@Excel(name = "雷达水位计空高(cm)", width = 15)
    @ApiModelProperty(value = "雷达水位计空高(cm)")
    private java.lang.String fluviograph;
	/**累积雨量(mm)*/
	@Excel(name = "累积雨量(mm)", width = 15)
    @ApiModelProperty(value = "累积雨量(mm)")
    private java.lang.String acrainfall;
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
	@Excel(name = "数据保存的时间点", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据保存的时间点")
    private java.util.Date timevalue;
	/**雷达水位计液位高（cm）*/
	@Excel(name = "雷达水位计液位高（cm）", width = 15)
    @ApiModelProperty(value = "雷达水位计液位高（cm）")
    private java.lang.String radar;
	/**瞬时雨量mm*/
	@Excel(name = "瞬时雨量mm", width = 15)
    @ApiModelProperty(value = "瞬时雨量mm")
    private java.lang.String insrainfall;
	/**当前雨量（今日雨量）mm*/
	@Excel(name = "当前雨量（今日雨量）mm", width = 15)
    @ApiModelProperty(value = "当前雨量（今日雨量）mm")
    private java.lang.String overproofStatus;
	/**日雨量（昨日雨量）mm*/
	@Excel(name = "日雨量（昨日雨量）mm", width = 15)
    @ApiModelProperty(value = "日雨量（昨日雨量）mm")
    private java.lang.String istuisong;
	/**预警原因*/
	@Excel(name = "预警原因", width = 15)
    @ApiModelProperty(value = "预警原因")
    private java.lang.String overproofInfo;
}
