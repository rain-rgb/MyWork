package com.trtm.iot.devicetunnelenvironmentdata.entity;

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
 * @Description: 隧道环境（有害气体）监测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Data
@TableName("device_tunnel_environmentdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_tunnel_environmentdata对象", description="隧道环境（有害气体）监测主表")
public class DeviceTunnelEnvironmentdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**地点*/
	@Excel(name = "地点", width = 15)
    @ApiModelProperty(value = "地点")
    private java.lang.String site;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date datatime;
	/**0无报警，1低报警，2高报警*/
	@Excel(name = "0无报警，1低报警，2高报警", width = 15)
    @ApiModelProperty(value = "0无报警，1低报警，2高报警")
    @Dict(dicCode = "alarmType")
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
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.lang.String uploadtime;
    private java.lang.String windspeed;
    private java.lang.String temperature;
    private java.lang.String guid;
    private java.lang.String dust;
    private java.lang.String noise;
    private java.lang.String pm1;
    private java.lang.String pm2;
    private java.lang.String pm10;
    /**湿度*/
    @Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private java.lang.String humidity;
    private Integer overproofStatus;//审核状态
    @ApiModelProperty(value = "项目名称", dataType = "String")
    private java.lang.String projectid;

    @ApiModelProperty(value = "标段", dataType = "String")
    private java.lang.String sectionid;

    @ApiModelProperty(value = "隧道名称", dataType = "String")
    private java.lang.String tunnelid;

    @ApiModelProperty(value = "二氧化硫", dataType = "String")
    private java.lang.String sulfurDioxide;

}
