package com.trtm.iot.clkreal.entity;

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
 * @Description: 出料口测温实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-06
 * @Version: V1.0
 */
@Data
@TableName("front_dischargehole_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="front_dischargehole_realdata对象", description="出料口测温实时数据表")
public class FrontDischargeholeRealdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**实际温度*/
	@Excel(name = "实际温度", width = 15)
    @ApiModelProperty(value = "实际温度")
    private java.lang.String val;
	/**originallng*/
	@Excel(name = "originallng", width = 15)
    @ApiModelProperty(value = "originallng")
    private java.lang.String originallng;
	/**unit*/
	@Excel(name = "unit", width = 15)
    @ApiModelProperty(value = "unit")
    private java.lang.String unit;
	/**lng*/
	@Excel(name = "lng", width = 15)
    @ApiModelProperty(value = "lng")
    private java.lang.String lng;
	/**sensorname*/
	@Excel(name = "sensorname", width = 15)
    @ApiModelProperty(value = "sensorname")
    private java.lang.String sensorname;
	/**上传时间*/
	@Excel(name = "上传时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上传时间")
    private java.util.Date updatetime;
	/**originallat*/
	@Excel(name = "originallat", width = 15)
    @ApiModelProperty(value = "originallat")
    private java.lang.String originallat;
	/**switcher*/
	@Excel(name = "switcher", width = 15)
    @ApiModelProperty(value = "switcher")
    private java.lang.String switcher;
	/**devicename*/
	@Excel(name = "devicename", width = 15)
    @ApiModelProperty(value = "devicename")
    private java.lang.String devicename;
	/**sensortypeid*/
	@Excel(name = "sensortypeid", width = 15)
    @ApiModelProperty(value = "sensortypeid")
    private java.lang.Integer sensortypeid;
	/**lat*/
	@Excel(name = "lat", width = 15)
    @ApiModelProperty(value = "lat")
    private java.lang.String lat;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String sensorid;
	/**deviceid*/
	@Excel(name = "deviceid", width = 15)
    @ApiModelProperty(value = "deviceid")
    private java.lang.Integer deviceid;
}
