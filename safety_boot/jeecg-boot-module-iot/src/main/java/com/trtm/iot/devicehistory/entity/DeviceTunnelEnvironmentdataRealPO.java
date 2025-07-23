package com.trtm.iot.devicehistory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author dupc
 * @since 2022-07-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("device_tunnel_environmentdata_real")
@ApiModel(value = "DeviceTunnelEnvironmentdataRealPO对象", description = "")
public class DeviceTunnelEnvironmentdataRealPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "地点", dataType = "String")
    @TableField("site")
    private String site;

    @ApiModelProperty(value = "设备编号", dataType = "String")
    @TableField("shebeino")
    private String shebeino;

    @ApiModelProperty(value = "数据上传时间", dataType = "Date")
    @TableField("dataTime")
    private Date dataTime;

    @ApiModelProperty(value = "0无报警，1低报警，2高报警", dataType = "Integer")
    @TableField("alarmType")
    private Integer alarmType;

    @ApiModelProperty(value = "氧气", dataType = "String")
    @TableField("oxygen")
    private String oxygen;

    @ApiModelProperty(value = "甲烷", dataType = "String")
    @TableField("methane")
    private String methane;

    @ApiModelProperty(value = "硫化氢", dataType = "String")
    @TableField("H_sulfide")
    private String hSulfide;

    @ApiModelProperty(value = "二氧化碳", dataType = "String")
    @TableField("C_dioxide")
    private String cDioxide;

    @ApiModelProperty(value = "一氧化碳", dataType = "String")
    @TableField("C_monoxide")
    private String cMonoxide;

    @ApiModelProperty(value = "风速", dataType = "String")
    @TableField("windspeed")
    private String windspeed;

    @ApiModelProperty(value = "数据时间", dataType = "String")
    @TableField("uploadTime")
    private String uploadTime;

    @ApiModelProperty(value = "温度", dataType = "String")
    @TableField("temperature")
    private String temperature;

    @TableField("guid")
    private String guid;

    @ApiModelProperty(value = "粉尘", dataType = "String")
    @TableField("dust")
    private String dust;

    @ApiModelProperty(value = "噪声", dataType = "String")
    @TableField("noise")
    private String noise;


    @ApiModelProperty(value = "粉尘(PM1.0)", dataType = "String")
    @TableField("PM1")
    private String pmOne;

    @ApiModelProperty(value = "粉尘(PM2.5)", dataType = "String")
    @TableField("PM2")
    private String pmTwo;

    @ApiModelProperty(value = "粉尘(PM10)", dataType = "String")
    @TableField("PM10")
    private String pmTen;

    @TableField("buss_id")
    private String bussId;

    @ApiModelProperty(value = "湿度", dataType = "String")
    @TableField("humidity")
    private String humidity;

    @ApiModelProperty(value = "项目名称", dataType = "String")
    @TableField("projectid")
    private String projectid;

    @ApiModelProperty(value = "标段", dataType = "String")
    @TableField("sectionid")
    private String sectionid;

    @ApiModelProperty(value = "隧道名称", dataType = "String")
    @TableField("tunnelid")
    private String tunnelid;

    @ApiModelProperty(value = "二氧化硫", dataType = "String")
    @TableField("sulfur_dioxide")
    private String sulfurDioxide;

    @ApiModelProperty(value = "气体采集信息", dataType = "String")
    @TableField(exist = false)
    private String alarmInfo;

}
