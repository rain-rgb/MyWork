package com.trtm.iot.devicetiebeamhistorydata.entity;

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
 * @Description: 提梁机历史数据信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Data
@TableName("device_tiebeam_historydata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_tiebeam_historydata对象", description="提梁机历史数据信息表")
public class DeviceTiebeamHistorydata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键")
	private java.lang.Integer id;
	/**命令*/
	@Excel(name = "命令", width = 15)
	@ApiModelProperty(value = "命令")
	private java.lang.Integer cmd;
	/**消息发送时间戳*/
	@Excel(name = "消息发送时间戳", width = 15)
	@ApiModelProperty(value = "消息发送时间戳")
	private java.lang.Integer time;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
	@ApiModelProperty(value = "设备类型")
	private java.lang.Integer devtype;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
	@ApiModelProperty(value = "设备编号")
	@Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
	private java.lang.String devno;
	/**前小车进程单位：m100倍放大*/
	@Excel(name = "前小车进程单位：m100倍放大", width = 15)
	@ApiModelProperty(value = "前小车进程单位：m100倍放大")
	private java.lang.Double frontcardistance;
	/**钩1额重单位：t100倍放大*/
	@Excel(name = "钩1额重单位：t100倍放大", width = 15)
	@ApiModelProperty(value = "钩1额重单位：t100倍放大")
	private java.lang.Double hook1frontalweight;
	/**钩2额重单位：t100倍放大*/
	@Excel(name = "钩1重量单位：t100倍放大", width = 15)
	@ApiModelProperty(value = "钩1重量单位：t100倍放大")
	private java.lang.Double hook1weight;
	/**钩1高度单位：m100倍放大*/
	@Excel(name = "钩1高度单位：m100倍放大", width = 15)
	@ApiModelProperty(value = "钩1高度单位：m100倍放大")
	private java.lang.Double hook1height;
	/**后小车进程单位：m100倍放大*/
	@Excel(name = "后小车进程单位：m100倍放大", width = 15)
	@ApiModelProperty(value = "后小车进程单位：m100倍放大")
	private java.lang.Double backcardistance;
	/**钩2额重单位：t100倍放大*/
	@Excel(name = "钩2额重单位：t100倍放大", width = 15)
	@ApiModelProperty(value = "钩2额重单位：t100倍放大")
	private java.lang.Double hook2frontalweight;
	/**钩2重量单位：t100倍放大*/
	@Excel(name = "钩2重量单位：t100倍放大", width = 15)
	@ApiModelProperty(value = "钩2重量单位：t100倍放大")
	private java.lang.Double hook2weight;
	/**钩2高度单位：m100倍放大*/
	@Excel(name = "钩2高度单位：m100倍放大", width = 15)
	@ApiModelProperty(value = "钩2高度单位：m100倍放大")
	private java.lang.Double hook2height;
	/**风速单位：m/s100倍放大*/
	@Excel(name = "风速单位：m/s100倍放大", width = 15)
	@ApiModelProperty(value = "风速单位：m/s100倍放大")
	private java.lang.Double windspeed;
	/**风级*/
	@Excel(name = "风级", width = 15)
	@ApiModelProperty(value = "风级")
	private java.lang.Double windlevel;
	/**大车进程单位：m100倍放大*/
	@Excel(name = "大车进程单位：m100倍放大", width = 15)
	@ApiModelProperty(value = "大车进程单位：m100倍放大")
	private java.lang.Double bigcardistance;

	private java.lang.String bigx;
	private java.lang.String bigy;
	private java.lang.String verticalx;
	private java.lang.String verticaly;
	private java.lang.Double slr1;
	private java.lang.Double slr2;
}
