package com.trtm.iot.device.entity;

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
 * @Description: 环境监测实时数据
 * @Author: jeecg-boot
 * @Date:   2022-04-11
 * @Version: V1.0
 */
@Data
@TableName("device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device对象", description="环境监测实时数据")
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;

	/**唯一标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.Integer id;
	/**设备唯一ID*/
	@Excel(name = "设备唯一ID", width = 15)
    @ApiModelProperty(value = "设备唯一ID")
    private java.lang.Integer devkey;
	/**设备名*/
	@Excel(name = "设备名", width = 15)
    @ApiModelProperty(value = "设备名")
    private java.lang.String devname;
	/**0为模拟量，1为开关量*/
	@Excel(name = "0为模拟量，1为开关量", width = 15)
    @ApiModelProperty(value = "0为模拟量，1为开关量")
    private java.lang.Integer devtype;
	/**设备地址(也是设备注册时的编号)*/
	@Excel(name = "设备地址(也是设备注册时的编号)", width = 15)
    @ApiModelProperty(value = "设备地址(也是设备注册时的编号)")
    private java.lang.String devaddr;
	/**模拟量一名称*/
	@Excel(name = "模拟量一名称", width = 15)
    @ApiModelProperty(value = "模拟量一名称")
    private java.lang.String devtempname;
	/**模拟量一的值*/
	@Excel(name = "模拟量一的值", width = 15)
    @ApiModelProperty(value = "模拟量一的值")
    private java.lang.String devtempvalue;
	/**模拟量二名称*/
	@Excel(name = "模拟量二名称", width = 15)
    @ApiModelProperty(value = "模拟量二名称")
    private java.lang.String devhuminame;
	/**模拟量二值*/
	@Excel(name = "模拟量二值", width = 15)
    @ApiModelProperty(value = "模拟量二值")
    private java.lang.String devhumivalue;
	/**设备状态  flase表示离线，true 表示在线*/
	@Excel(name = "设备状态  flase表示离线，true 表示在线", width = 15)
    @ApiModelProperty(value = "设备状态  flase表示离线，true 表示在线")
    private java.lang.String devstatus;
	/**设备所处纬度*/
	@Excel(name = "设备所处纬度", width = 15)
    @ApiModelProperty(value = "设备所处纬度")
    private java.math.BigDecimal devlng;
	/**设备所处经度*/
	@Excel(name = "设备所处经度", width = 15)
    @ApiModelProperty(value = "设备所处经度")
    private java.math.BigDecimal devlat;
	/**模拟量一报警状态（0 表示不报警，1 表示超上限，2 表示超下限）*/
	@Excel(name = "模拟量一报警状态（0 表示不报警，1 表示超上限，2 表示超下限）", width = 15)
    @ApiModelProperty(value = "模拟量一报警状态（0 表示不报警，1 表示超上限，2 表示超下限）")
    private java.lang.Integer tempstatus;
	/**模拟量二报警状态（0 表示不报警，1 表示超上限，2 表示超下限）*/
	@Excel(name = "模拟量二报警状态（0 表示不报警，1 表示超上限，2 表示超下限）", width = 15)
    @ApiModelProperty(value = "模拟量二报警状态（0 表示不报警，1 表示超上限，2 表示超下限）")
    private java.lang.Integer humistatus;
	/**模拟量一相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）*/
	@Excel(name = "模拟量一相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）", width = 15)
    @ApiModelProperty(value = "模拟量一相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）")
    private java.lang.String devdatatype1;
	/**模拟量二相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）*/
	@Excel(name = "模拟量二相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）", width = 15)
    @ApiModelProperty(value = "模拟量二相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）")
    private java.lang.String devdatatype2;
	/**设备节点号*/
	@Excel(name = "设备节点号", width = 15)
    @ApiModelProperty(value = "设备节点号")
    private java.lang.Integer devpos;
	/**数据保存的时间点*/
	@Excel(name = "数据保存的时间点", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据保存的时间点")
    private java.util.Date timevalue;
}
