package com.trtm.iot.devicetunnelposition.entity;

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
 * @Description: 隧道人员定位表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
@Data
@TableName("device_tunnel_positiondata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_tunnel_positiondata对象", description="隧道人员定位表信息")
public class DeviceTunnelPositiondata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**人员姓名*/
	@Excel(name = "人员姓名", width = 15)
    @ApiModelProperty(value = "人员姓名")
    private java.lang.String username;
	/**卡号*/
	@Excel(name = "卡号", width = 15)
    @ApiModelProperty(value = "卡号")
    private java.lang.String card;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date datatime;
	/**距离洞口距离*/
	@Excel(name = "距离洞口距离", width = 15)
    @ApiModelProperty(value = "距离洞口距离")
    private java.lang.Double dv;
	/**定位时间*/
	@Excel(name = "定位时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "定位时间")
    private java.util.Date dvtime;
	/**定位基站名称*/
	@Excel(name = "定位基站名称", width = 15)
    @ApiModelProperty(value = "定位基站名称")
    private java.lang.String jz;
	/**距离基站距离*/
	@Excel(name = "距离基站距离", width = 15)
    @ApiModelProperty(value = "距离基站距离")
    private java.lang.Double jzdv;
	/**基站距离洞口距离*/
	@Excel(name = "基站距离洞口距离", width = 15)
    @ApiModelProperty(value = "基站距离洞口距离")
    private java.lang.Double jzkou;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**基站位置*/
	@Excel(name = "基站位置", width = 15)
    @ApiModelProperty(value = "基站位置")
    private java.lang.String jzwz;
	/**班组编号*/
	@Excel(name = "班组编号", width = 15)
    @ApiModelProperty(value = "班组编号")
    private java.lang.String departmentid;
	/**班组名称*/
	@Excel(name = "班组名称", width = 15)
    @ApiModelProperty(value = "班组名称")
    private java.lang.String departname;
	/**X坐标*/
	@Excel(name = "X坐标", width = 15)
    @ApiModelProperty(value = "X坐标")
    private java.lang.Double nx;
	/**Y坐标*/
	@Excel(name = "Y坐标", width = 15)
    @ApiModelProperty(value = "Y坐标")
    private java.lang.Double ny;
	/**电池电量*/
	@Excel(name = "电池电量", width = 15)
    @ApiModelProperty(value = "电池电量")
    private java.lang.Double voltages;
	/**报警状态0：无报警，1：按钮报警*/
	@Excel(name = "报警状态0：无报警，1：按钮报警", width = 15)
    @ApiModelProperty(value = "报警状态0：无报警，1：按钮报警")
    private java.lang.Integer alarms;
	/**上报时间*/
	@Excel(name = "上报时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上报时间")
    private java.util.Date uploadtime;
}
