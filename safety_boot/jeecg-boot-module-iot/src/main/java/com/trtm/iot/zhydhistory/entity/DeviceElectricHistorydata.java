package com.trtm.iot.zhydhistory.entity;

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
 * @Description: 智慧用电历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
@Data
@TableName("device_electric_historydata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_electric_historydata对象", description="智慧用电历史数据表")
public class DeviceElectricHistorydata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private java.lang.String imei;
	/**电压A*/
	@Excel(name = "电压A", width = 15)
    @ApiModelProperty(value = "电压A")
    private java.lang.Double voltagea;
	/**电压B*/
	@Excel(name = "电压B", width = 15)
    @ApiModelProperty(value = "电压B")
    private java.lang.Double voltageb;
	/**电压C*/
	@Excel(name = "电压C", width = 15)
    @ApiModelProperty(value = "电压C")
    private java.lang.Double voltagec;
	/**频率A*/
	@Excel(name = "频率A", width = 15)
    @ApiModelProperty(value = "频率A")
    private java.lang.Double frequencya;
	/**频率B*/
	@Excel(name = "频率B", width = 15)
    @ApiModelProperty(value = "频率B")
    private java.lang.Double frequencyb;
	/**频率C*/
	@Excel(name = "频率C", width = 15)
    @ApiModelProperty(value = "频率C")
    private java.lang.Double frequencyc;
	/**电流A*/
	@Excel(name = "电流A", width = 15)
    @ApiModelProperty(value = "电流A")
    private java.lang.Double electricitya;
	/**电流B*/
	@Excel(name = "电流B", width = 15)
    @ApiModelProperty(value = "电流B")
    private java.lang.Double electricityb;
	/**电流C*/
	@Excel(name = "电流C", width = 15)
    @ApiModelProperty(value = "电流C")
    private java.lang.Double electricityc;
	/**漏电流*/
	@Excel(name = "漏电流", width = 15)
    @ApiModelProperty(value = "漏电流")
    private java.lang.Double seepelectricity;
	/**温度A*/
	@Excel(name = "温度A", width = 15)
    @ApiModelProperty(value = "温度A")
    private java.lang.Double tempa;
	/**温度B*/
	@Excel(name = "温度B", width = 15)
    @ApiModelProperty(value = "温度B")
    private java.lang.Double tempb;
	/**温度C*/
	@Excel(name = "温度C", width = 15)
    @ApiModelProperty(value = "温度C")
    private java.lang.Double tempc;
	/**温度N*/
	@Excel(name = "温度N", width = 15)
    @ApiModelProperty(value = "温度N")
    private java.lang.Double tempn;
	/**电能A*/
	@Excel(name = "电能A", width = 15)
    @ApiModelProperty(value = "电能A")
    private java.lang.Double energya;
	/**电能B*/
	@Excel(name = "电能B", width = 15)
    @ApiModelProperty(value = "电能B")
    private java.lang.Double energyb;
	/**电能C*/
	@Excel(name = "电能C", width = 15)
    @ApiModelProperty(value = "电能C")
    private java.lang.Double energyc;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date elecdate;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date datatime;
    @Excel(name = "状态", width = 3)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    @Excel(name = "是否超标", width = 10)
    @ApiModelProperty(value = "是否超标")
    private java.lang.Integer alertstate;
    @Excel(name = "电压A状态", width = 15)
    @ApiModelProperty(value = "电压A状态")
    private java.lang.Integer vaStatus;
    @Excel(name = "电压B状态", width = 15)
    @ApiModelProperty(value = "电压B状态")
    private java.lang.Integer vbStatus;
    @Excel(name = "电压C状态", width = 15)
    @ApiModelProperty(value = "电压C状态")
    private java.lang.Integer vcStatus;
    @Excel(name = "电流A状态", width = 15)
    @ApiModelProperty(value = "电流A状态")
    private java.lang.Integer eaStatus;
    @Excel(name = "电流B状态", width = 15)
    @ApiModelProperty(value = "电流B状态")
    private java.lang.Integer ebStatus;
    @Excel(name = "电流C状态", width = 15)
    @ApiModelProperty(value = "电流C状态")
    private java.lang.Integer ecStatus;
    @Excel(name = "漏电流状态", width = 15)
    @ApiModelProperty(value = "漏电流状态")
    private java.lang.Integer seStatus;
    @Excel(name = "温度A状态", width = 15)
    @ApiModelProperty(value = "温度A状态")
    private java.lang.Integer taStatus;
    @Excel(name = "温度B状态", width = 15)
    @ApiModelProperty(value = "温度B状态")
    private java.lang.Integer tbStatus;
    @Excel(name = "温度C状态", width = 15)
    @ApiModelProperty(value = "温度C状态")
    private java.lang.Integer tcStatus;
    @Excel(name = "频率A状态", width = 15)
    @ApiModelProperty(value = "频率A状态")
    private java.lang.Integer faStatus;
    @Excel(name = "频率B状态", width = 15)
    @ApiModelProperty(value = "频率B状态")
    private java.lang.Integer fbStatus;
    @Excel(name = "频率C状态", width = 15)
    @ApiModelProperty(value = "频率C状态")
    private java.lang.Integer fcStatus;
    private Integer overproofStatus;//审核状态

}
