package com.trtm.iot.zhydstatistics.entity;

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
 * @Description: 智慧用电历史数据统计表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-02
 * @Version: V1.0
 */
@Data
@TableName("device_electrc_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_electrc_statistics对象", description="智慧用电历史数据统计表信息")
public class DeviceElectrcStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String imei;
	/**电压超标总数*/
	@Excel(name = "电压超标总数", width = 15)
    @ApiModelProperty(value = "电压超标总数")
    private java.lang.Integer voltage;
	/**频率超标总数*/
	@Excel(name = "频率超标总数", width = 15)
    @ApiModelProperty(value = "频率超标总数")
    private java.lang.Integer frequency;
	/**电流超标总数*/
	@Excel(name = "电流超标总数", width = 15)
    @ApiModelProperty(value = "电流超标总数")
    private java.lang.Integer electricity;
	/**漏电流超标总数*/
	@Excel(name = "漏电流超标总数", width = 15)
    @ApiModelProperty(value = "漏电流超标总数")
    private java.lang.Integer seepelectricity;
	/**温度超标总数*/
	@Excel(name = "温度超标总数", width = 15)
    @ApiModelProperty(value = "温度超标总数")
    private java.lang.Integer temp;
    /**电压A超标数*/
    @Excel(name = "电压A超标数", width = 15)
    @ApiModelProperty(value = "电压A超标数")
    private java.lang.Integer voltagea;
    /**电压B超标数*/
    @Excel(name = "电压B超标数", width = 15)
    @ApiModelProperty(value = "电压B超标数")
    private java.lang.Integer voltageb;
    /**电压C超标数*/
    @Excel(name = "电压C超标数", width = 15)
    @ApiModelProperty(value = "电压C超标数")
    private java.lang.Integer voltagec;
    /**频率A超标数*/
    @Excel(name = "频率A超标数", width = 15)
    @ApiModelProperty(value = "频率A超标数")
    private java.lang.Integer frequencya;
    /**频率B超标数*/
    @Excel(name = "频率B超标数", width = 15)
    @ApiModelProperty(value = "频率B超标数")
    private java.lang.Integer frequencyb;
    /**频率C超标数*/
    @Excel(name = "频率C超标数", width = 15)
    @ApiModelProperty(value = "频率C超标数")
    private java.lang.Integer frequencyc;
    /**电流A超标数*/
    @Excel(name = "电流A超标数", width = 15)
    @ApiModelProperty(value = "电流A超标数")
    private java.lang.Integer electricitya;
    /**电流B超标数*/
    @Excel(name = "电流B超标数", width = 15)
    @ApiModelProperty(value = "电流B超标数")
    private java.lang.Integer electricityb;
    /**电流C超标数*/
    @Excel(name = "电流C超标数", width = 15)
    @ApiModelProperty(value = "电流C超标数")
    private java.lang.Integer electricityc;
    /**温度A超标数*/
    @Excel(name = "温度A超标数", width = 15)
    @ApiModelProperty(value = "温度A超标数")
    private java.lang.Integer tempa;
    /**温度B超标数*/
    @Excel(name = "温度B超标数", width = 15)
    @ApiModelProperty(value = "温度B超标数")
    private java.lang.Integer tempb;
    /**温度C超标数*/
    @Excel(name = "温度C超标数", width = 15)
    @ApiModelProperty(value = "温度C超标数")
    private java.lang.Integer tempc;
	/**统计时间，以天为单位*/
	@Excel(name = "统计时间，以天为单位", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计时间，以天为单位")
    private java.util.Date statisticsTime;
}
