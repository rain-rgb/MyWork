package com.trtm.iot.devicehighformwork.entity;

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
 * @Description: device_high_formwork_real
 * @Author: jeecg-boot
 * @Date:   2023-11-21
 * @Version: V1.0
 */
@Data
@TableName("device_high_formwork_real")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_high_formwork_real对象", description="device_high_formwork_real")
public class DeviceHighFormworkReal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备类型:1位移设备，2轴力设备，3倾角设备*/
	@Excel(name = "设备类型:1位移设备，2轴力设备，3倾角设备", width = 15)
    @ApiModelProperty(value = "设备类型:1位移设备，2轴力设备，3倾角设备")
    private java.lang.String sntype;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String snbh;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    private java.lang.String version;
	/**检查时间*/
	@Excel(name = "检查时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检查时间")
    private java.util.Date detecttime;
	/**设备电量*/
	@Excel(name = "设备电量", width = 15)
    @ApiModelProperty(value = "设备电量")
    private java.lang.String batteryPercentage;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.String temperature;
	/**位移绝对值/轴力绝对值/X轴绝对角度*/
	@Excel(name = "位移绝对值/轴力绝对值/X轴绝对角度", width = 15)
    @ApiModelProperty(value = "位移绝对值/轴力绝对值/X轴绝对角度")
    private java.lang.String abparam1;
	/**Y轴绝对角度*/
	@Excel(name = "Y轴绝对角度", width = 15)
    @ApiModelProperty(value = "Y轴绝对角度")
    private java.lang.String abparam2;
	/**预留参数3*/
	@Excel(name = "预留参数3", width = 15)
    @ApiModelProperty(value = "预留参数3")
    private java.lang.String abparam3;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检查时间")
    private java.util.Date createTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检查时间")
    private java.util.Date updateTime;

}
