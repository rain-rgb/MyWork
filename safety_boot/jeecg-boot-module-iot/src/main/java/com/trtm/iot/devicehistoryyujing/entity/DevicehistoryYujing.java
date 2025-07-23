package com.trtm.iot.devicehistoryyujing.entity;

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
 * @Description: 预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-06-23
 * @Version: V1.0
 */
@Data
@TableName("devicehistory_yujing")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="devicehistory_yujing对象", description="预警配置表")
public class DevicehistoryYujing implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备地址*/
	@Excel(name = "设备地址", width = 15)
    @ApiModelProperty(value = "设备地址")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String devaddr;
    /**PM10(ug/m³)(初级标准值)*/
    @Excel(name = "PM10(ug/m³)(初级标准值)", width = 15)
    @ApiModelProperty(value = "PM10(ug/m³)(初级标准值)")
    private java.lang.String pm10y;
	/**PM10(ug/m³)(中级标准值)*/
	@Excel(name = "PM10(ug/m³)(中级标准值)", width = 15)
    @ApiModelProperty(value = "PM10(ug/m³)(中级标准值)")
    private java.lang.String pm10l;
    /**PM10(ug/m³)(高级标准值)*/
    @Excel(name = "PM10(ug/m³)(高级标准值)", width = 15)
    @ApiModelProperty(value = "PM10(ug/m³)(高级标准值)")
    private java.lang.String pm10c;
    /**PM2.5(ug/m³)(初级标准值)*/
    @Excel(name = "PM2.5(ug/m³)(初级标准值)", width = 15)
    @ApiModelProperty(value = "PM2.5(ug/m³)(初级标准值)")
    private java.lang.String pm25y;
	/**PM2.5(ug/m³)(中级标准值)*/
	@Excel(name = "PM2.5(ug/m³)(中级标准值)", width = 15)
    @ApiModelProperty(value = "PM2.5(ug/m³)(中级标准值)")
    private java.lang.String pm25l;
    /**PM2.5(ug/m³)(高级标准值)*/
    @Excel(name = "PM2.5(ug/m³)(高级标准值)", width = 15)
    @ApiModelProperty(value = "PM2.5(ug/m³)(高级标准值)")
    private java.lang.String pm25c;
    /**噪声(dB)(白天初级标准值)*/
    @Excel(name = "噪声(dB)(白天初级标准值)", width = 15)
    @ApiModelProperty(value = "噪声(dB)(白天初级标准值)")
    private java.lang.String noisey;
	/**噪声(dB)(白天中级标准值)*/
	@Excel(name = "噪声(dB)(白天中级标准值)", width = 15)
    @ApiModelProperty(value = "噪声(dB)(白天中级标准值)")
    private java.lang.String noisel;
    /**噪声(dB)(白天高级标准值)*/
    @Excel(name = "噪声(dB)(白天高级标准值)", width = 15)
    @ApiModelProperty(value = "噪声(dB)(白天高级标准值)")
    private java.lang.String noisec;
    /**噪声(dB)(夜间初级标准值)*/
    @Excel(name = "噪声(dB)(夜间初级标准值)", width = 15)
    @ApiModelProperty(value = "噪声(dB)(夜间初级标准值)")
    private java.lang.String noiseyn;
    /**噪声(dB)(夜间中级标准值)*/
    @Excel(name = "噪声(dB)(夜间中级标准值)", width = 15)
    @ApiModelProperty(value = "噪声(dB)(夜间中级标准值)")
    private java.lang.String noiseln;
    /**噪声(dB)(夜间高级标准值)*/
    @Excel(name = "噪声(dB)(夜间高级标准值)", width = 15)
    @ApiModelProperty(value = "噪声(dB)(夜间高级标准值)")
    private java.lang.String noisecn;
	/**温度(℃)*/
	@Excel(name = "温度(℃)", width = 15)
    @ApiModelProperty(value = "温度(℃)")
    private java.lang.String temperature;
	/**湿度(%RH)标准值*/
	@Excel(name = "湿度(%RH)标准值", width = 15)
    @ApiModelProperty(value = "湿度(%RH)标准值")
    private java.lang.String humidity;
	/**风力（最高18）*/
	@Excel(name = "风力标准值", width = 15)
    @ApiModelProperty(value = "风力标准值）")
    private java.lang.String wind;
	/**风速(m/s)标准值*/
	@Excel(name = "风速(m/s)标准值", width = 15)
    @ApiModelProperty(value = "风速(m/s)标准值")
    private java.lang.String windspeed;
	/**风向标准值*/
	@Excel(name = "风向标准值", width = 15)
    @ApiModelProperty(value = "风向标准值")
    private java.lang.String winddirection;
	/**数据保存的时间点*/
	@Excel(name = "数据保存的时间点", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据保存的时间点")
    private java.util.Date timevalue;
	/**是否删除（0：未删除，1：已删除）*/
	@Excel(name = "是否删除（0：未删除，1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private java.lang.Integer isdel;
	/**是否报警:0=报警,1=不报警*/
	@Excel(name = "是否报警:0=报警,1=不报警", width = 15)
    @ApiModelProperty(value = "是否报警:0=报警,1=不报警")
    @Dict(dicCode = "is_call")
    private java.lang.Integer isCall;
	/**初级预警号码组*/
	@Excel(name = "初级预警号码组", width = 15)
    @ApiModelProperty(value = "初级预警号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String primaryPhones;
    /**中级预警号码组*/
    @Excel(name = "中级预警号码组", width = 15)
    @ApiModelProperty(value = "中级预警号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String middlePhones;
    /**高级预警号码组*/
    @Excel(name = "高级预警号码组", width = 15)
    @ApiModelProperty(value = "高级预警号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String advancePhones;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
}
