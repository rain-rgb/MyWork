package com.trtm.iot.wbshebeihistory.entity;

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
 * @Description: 电子锁历史数据表
 * @Author: jeecg-boot
 * @Date:   2022-02-22
 * @Version: V1.0
 */
@Data
@TableName("wbshebei_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wbshebei_history对象", description="电子锁历史数据表")
public class WbshebeiHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String teid;
	/**时间*/
	@Excel(name = "时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private java.util.Date time;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String lat;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String lng;
	/**方向*/
	@Excel(name = "方向", width = 15)
    @ApiModelProperty(value = "方向")
    private java.lang.Integer direction;
	/**高度*/
	@Excel(name = "高度", width = 15)
    @ApiModelProperty(value = "高度")
    private java.lang.Double height;
	/**车辆状态*/
	@Excel(name = "车辆状态", width = 15)
    @ApiModelProperty(value = "车辆状态")
    private java.lang.Double carState;
	/**teState*/
	@Excel(name = "teState", width = 15)
    @ApiModelProperty(value = "teState")
    private java.lang.Double teState;
	/**预警状态*/
	@Excel(name = "预警状态", width = 15)
    @ApiModelProperty(value = "预警状态")
    private java.lang.Double alarmState;
	/**电量*/
	@Excel(name = "电量", width = 15)
    @ApiModelProperty(value = "电量")
    private java.lang.Double batteryVol;
	/**基站*/
	@Excel(name = "基站", width = 15)
    @ApiModelProperty(value = "基站")
    private java.lang.String baseStation;
	/**数据包类型*/
	@Excel(name = "数据包类型", width = 15)
    @ApiModelProperty(value = "数据包类型")
    private java.lang.String packetType;
	/**sublock*/
	@Excel(name = "sublock", width = 15)
    @ApiModelProperty(value = "sublock")
    private java.lang.String sublock;
	/**业务编号*/
	@Excel(name = "业务编号", width = 15)
    @ApiModelProperty(value = "业务编号")
    private java.lang.String businessId;
	/**射频识别*/
	@Excel(name = "射频识别", width = 15)
    @ApiModelProperty(value = "射频识别")
    private java.lang.String rfid;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.Integer temp;
	/**湿度*/
	@Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private java.lang.Double humidity;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.Double mileage;
	/**油量*/
	@Excel(name = "油量", width = 15)
    @ApiModelProperty(value = "油量")
    private java.lang.Double fuel;
}
