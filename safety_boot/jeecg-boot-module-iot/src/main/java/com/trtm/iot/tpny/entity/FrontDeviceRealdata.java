package com.trtm.iot.tpny.entity;

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
 * @Description: 摊铺碾压数据监测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Data
@TableName("front_device_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="front_device_realdata对象", description="摊铺碾压数据监测主表")
public class FrontDeviceRealdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备类型A:压路机、B:摊铺机*/
	@Excel(name = "设备类型A:压路机、B:摊铺机", width = 15)
    @ApiModelProperty(value = "设备类型A:压路机、B:摊铺机")
    @Dict(dicCode = "device_type")
    private java.lang.String deviceType;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;
	/**速度A:km/h B:m/min*/
	@Excel(name = "速度A:km/h B:m/min", width = 15)
    @ApiModelProperty(value = "速度A:km/h B:m/min")
    private java.lang.Double speed;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.Double temperature;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.Double longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.Double latitude;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date datatime;
    /**GPS卫星数*/
    @Excel(name = "GPS卫星数", width = 15)
    @ApiModelProperty(value = "GPS卫星数")
    private java.lang.Integer gpsnum;
    /**gps状态*/
    @Excel(name = "gps状态", width = 15)
    @ApiModelProperty(value = "gps状态")
    private java.lang.Integer gpsstatus;
    /**电源状态 */
    @Excel(name = "电源状态 ", width = 15)
    @ApiModelProperty(value = "电源状态 ")
    private java.lang.Integer switchstatus;
    /**航向*/
    @Excel(name = "航向", width = 15)
    @ApiModelProperty(value = "航向")
    private java.lang.Integer direction;
    /**高程*/
    @Excel(name = "高程", width = 15)
    @ApiModelProperty(value = "高程")
    private java.lang.Double highly;
    /**海拔*/
    @Excel(name = "海拔", width = 15)
    @ApiModelProperty(value = "海拔")
    private java.lang.Double altitude;
    /**终端时间*/
    @Excel(name = "终端时间", width = 15)
    @ApiModelProperty(value = "终端时间")
    private java.lang.String uploadtime;
    /**车辆IFRD*/
    @Excel(name = "车辆IFRD", width = 15)
    @ApiModelProperty(value = "车辆IFRD")
    private java.lang.String ifid;
    /**湿度*/
    @Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private java.math.BigDecimal humidity;
    /**风速*/
    @Excel(name = "风速", width = 15)
    @ApiModelProperty(value = "风速")
    private java.math.BigDecimal wind;

    private java.lang.Integer shebeistatus;
    private java.lang.Integer status;
    private java.lang.Integer endtime;
    private java.lang.Integer platformendtime;
    private java.lang.Integer activatetime;
    private java.lang.String statustimedesc;
    private java.lang.Integer signaltime;
    private java.lang.Integer gpstime;
    private java.lang.String positiontype;
    private java.lang.Integer iswireless;
    private java.lang.String sim;
    private java.lang.String cardnumber;



}
