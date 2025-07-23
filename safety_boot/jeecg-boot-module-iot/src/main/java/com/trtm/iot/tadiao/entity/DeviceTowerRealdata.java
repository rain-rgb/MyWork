package com.trtm.iot.tadiao.entity;

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
 * @Description: device_tower_realdata
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("device_tower_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_tower_realdata对象", description="device_tower_realdata")
public class DeviceTowerRealdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String deviceCode;
	/**设备类型2桥门机3架桥机*/
	@Excel(name = "设备类型2桥门机3架桥机", width = 15)
    @ApiModelProperty(value = "设备类型2桥门机3架桥机")
    private java.lang.Integer deviceType;
	/**主钩小车位移*/
	@Excel(name = "主钩小车位移", width = 15)
    @ApiModelProperty(value = "主钩小车位移")
    private java.lang.Double mainHookroute;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date towerdate;
	/**主钩吊钩高度*/
	@Excel(name = "主钩吊钩高度", width = 15)
    @ApiModelProperty(value = "主钩吊钩高度")
    private java.lang.Double mainHookheight;
	/**副钩吊钩高度*/
	@Excel(name = "副钩吊钩高度", width = 15)
    @ApiModelProperty(value = "副钩吊钩高度")
    private java.lang.Double reservedVicehookheight;
	/**主钩吊钩重量*/
	@Excel(name = "主钩吊钩重量", width = 15)
    @ApiModelProperty(value = "主钩吊钩重量")
    private java.lang.Double mainHookload;
	/**副钩吊钩重量*/
	@Excel(name = "副钩吊钩重量", width = 15)
    @ApiModelProperty(value = "副钩吊钩重量")
    private java.lang.Double reservedVicehookload;
	/**主钩力矩值*/
	@Excel(name = "主钩力矩值", width = 15)
    @ApiModelProperty(value = "主钩力矩值")
    private java.lang.Double mainHookmoment;
	/**副钩小车位移*/
	@Excel(name = "副钩小车位移", width = 15)
    @ApiModelProperty(value = "副钩小车位移")
    private java.lang.Double reservedViceroute;
	/**实时风速*/
	@Excel(name = "实时风速", width = 15)
    @ApiModelProperty(value = "实时风速")
    private java.lang.Double windSpeed;
	/**副钩力矩值*/
	@Excel(name = "副钩力矩值", width = 15)
    @ApiModelProperty(value = "副钩力矩值")
    private java.lang.Double reservedVicemoment;
	/**回转角度*/
	@Excel(name = "回转角度", width = 15)
    @ApiModelProperty(value = "回转角度")
    private java.lang.Double angleRotation;
	/**主钩吊钩载荷状态0重量正常1重量预警*/
	@Excel(name = "主钩吊钩载荷状态0重量正常1重量预警", width = 15)
    @ApiModelProperty(value = "主钩吊钩载荷状态0重量正常1重量预警")
    private java.lang.Integer mainHookstatus;
	/**副钩吊钩载荷状态0重量正常1重量预警*/
	@Excel(name = "副钩吊钩载荷状态0重量正常1重量预警", width = 15)
    @ApiModelProperty(value = "副钩吊钩载荷状态0重量正常1重量预警")
    private java.lang.Integer reservedVicehookstatus;
	/**主钩吊钩载荷状态0重量正常1吊重超重*/
	@Excel(name = "主钩吊钩载荷状态0重量正常1吊重超重", width = 15)
    @ApiModelProperty(value = "主钩吊钩载荷状态0重量正常1吊重超重")
    private java.lang.Integer mainHookstatus2;
	/**副钩吊钩载荷状态0重量正常1吊重超重*/
	@Excel(name = "副钩吊钩载荷状态0重量正常1吊重超重", width = 15)
    @ApiModelProperty(value = "副钩吊钩载荷状态0重量正常1吊重超重")
    private java.lang.Integer reservedVicehookstatus2;
	/**幅度*/
	@Excel(name = "幅度", width = 15)
    @ApiModelProperty(value = "幅度")
    private java.lang.Double amplitude;
	/**累计时间(单位小时)*/
	@Excel(name = "累计时间(单位小时)", width = 15)
    @ApiModelProperty(value = "累计时间(单位小时)")
    private java.lang.Integer allTime;
	/**循环使用次数*/
	@Excel(name = "循环使用次数", width = 15)
    @ApiModelProperty(value = "循环使用次数")
    private java.lang.Integer allTimes;
	/**碰撞危险*/
	@Excel(name = "碰撞危险", width = 15)
    @ApiModelProperty(value = "碰撞危险")
    private java.lang.Integer collisionHazard;
	/**预留*/
	@Excel(name = "预留", width = 15)
    @ApiModelProperty(value = "预留")
    private java.lang.Integer reservedOne;
	/**预留*/
	@Excel(name = "预留", width = 15)
    @ApiModelProperty(value = "预留")
    private java.lang.Integer reservedTwo;
	/**悬臂倾角*/
	@Excel(name = "悬臂倾角", width = 15)
    @ApiModelProperty(value = "悬臂倾角")
    private java.lang.Double cantileverAngle;
	/**倾斜*/
	@Excel(name = "倾斜", width = 15)
    @ApiModelProperty(value = "倾斜")
    private java.lang.Double tilt;
	/**主钩吊钩升降速度*/
	@Excel(name = "主钩吊钩升降速度", width = 15)
    @ApiModelProperty(value = "主钩吊钩升降速度")
    private java.lang.Double mainHookspeed;
	/**主钩小车移动速度*/
	@Excel(name = "主钩小车移动速度", width = 15)
    @ApiModelProperty(value = "主钩小车移动速度")
    private java.lang.Double mainCarspeed;
    /**副钩吊钩升降速度*/
    @Excel(name = "副钩吊钩升降速度", width = 15)
    @ApiModelProperty(value = "副钩吊钩升降速度")
    @TableField(value = "reserved_vicehookspeed")
    private java.lang.Double reservedVicehookspeed;
    /**副钩小车移动速度*/
    @Excel(name = "副钩小车移动速度", width = 15)
    @ApiModelProperty(value = "副钩小车移动速度")
    @TableField(value = "reserved_vicecarspeed")
    private java.lang.Double reservedVicecarspeed;
}
