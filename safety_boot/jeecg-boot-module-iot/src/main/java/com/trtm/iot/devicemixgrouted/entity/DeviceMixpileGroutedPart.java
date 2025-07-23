package com.trtm.iot.devicemixgrouted.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 灌注桩子表
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_grouted_part")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_grouted_part对象", description="灌注桩子表")
public class DeviceMixpileGroutedPart implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String sbbh;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String pileMileage;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileNo;
	/**段号*/
	@Excel(name = "段号", width = 15)
    @ApiModelProperty(value = "段号")
    private java.lang.String partid;
	/**密度*/
	@Excel(name = "密度", width = 15)
    @ApiModelProperty(value = "密度")
    private java.lang.String pileDensity;
	/**浆量*/
	@Excel(name = "浆量", width = 15)
    @ApiModelProperty(value = "浆量")
    private java.lang.String pileCement;
	/**时间*/
	@Excel(name = "时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "时间")
    private java.util.Date parttime;
	/**主表uuid*/
	@Excel(name = "主表uuid", width = 15)
    @ApiModelProperty(value = "主表uuid")
    private java.lang.String oneuuid;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**param1*/
	@Excel(name = "param1", width = 15)
    @ApiModelProperty(value = "param1")
    private java.lang.String param1;
	/**param2*/
	@Excel(name = "param2", width = 15)
    @ApiModelProperty(value = "param2")
    private java.lang.String param2;
    private String pileUspeed;// 上钻速度
    private String pileUelectr;// 上钻电流
    private String pileDspeed;// 下钻速度
    private String pileDelectr;// 下钻电流
    private java.lang.String pileX;
    private java.lang.String pileY;
}
