package com.trtm.iot.openapigpsdatavo.entity;

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
 * @Description: 摊铺碾压数据
 * @Author: jeecg-boot
 * @Date:   2023-04-21
 * @Version: V1.0
 */
@Data
@TableName("openapigpsdatavo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_openapigpsdatavo对象", description="摊铺碾压数据")
public class Openapigpsdatavo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "sjid")
    private java.lang.Integer sjid;
	/**机械终端安装关系id*/
	@Excel(name = "机械终端安装关系id", width = 15)
    @ApiModelProperty(value = "机械终端安装关系id")
    private String machineId;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private Date gpsTime;
	/**定位质量，1：单点、2：码差分、4：固定、5：浮动*/
	@Excel(name = "定位质量，1：单点、2：码差分、4：固定、5：浮动", width = 15)
    @ApiModelProperty(value = "定位质量，1：单点、2：码差分、4：固定、5：浮动")
    private String qualityIndex;
	/**北坐标*/
	@Excel(name = "北坐标", width = 15)
    @ApiModelProperty(value = "北坐标")
    private java.lang.String north;
	/**东坐标*/
	@Excel(name = "东坐标", width = 15)
    @ApiModelProperty(value = "东坐标")
    private java.lang.String east;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String lon;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String lat;
	/**摊铺温度*/
	@Excel(name = "摊铺温度", width = 15)
    @ApiModelProperty(value = "摊铺温度")
    private java.lang.String temperature;
	/**所在桩号*/
	@Excel(name = "所在桩号", width = 15)
    @ApiModelProperty(value = "所在桩号")
    private String roadStation;
	/**位置相对道路中线偏移量*/
	@Excel(name = "位置相对道路中线偏移量", width = 15)
    @ApiModelProperty(value = "位置相对道路中线偏移量")
    private java.lang.String offset;
	/**速度(m/s)*/
	@Excel(name = "速度(m/s)", width = 15)
    @ApiModelProperty(value = "速度(m/s)")
    private java.lang.String velocity;
	/**温度离析(℃)*/
	@Excel(name = "温度离析(℃)", width = 15)
    @ApiModelProperty(value = "温度离析(℃)")
    private String tempDiff;
	/**高程*/
	@Excel(name = "高程", width = 15)
    @ApiModelProperty(value = "高程")
    private java.lang.String geoh;
	/**压实度*/
	@Excel(name = "压实度", width = 15)
    @ApiModelProperty(value = "压实度")
    private java.lang.String cmv;
	/**振动频率*/
	@Excel(name = "振动频率", width = 15)
    @ApiModelProperty(value = "振动频率")
    private java.lang.String frequency;
	/**运料车rfid*/
	@Excel(name = "运料车rfid", width = 15)
    @ApiModelProperty(value = "运料车rfid")
    private java.lang.String rfid;
	/**所属层，层属于后处理计算结果，实时性一般，初始值为65535，计算后会将其更新*/
	@Excel(name = "所属层，层属于后处理计算结果，实时性一般，初始值为65535，计算后会将其更新", width = 15)
    @ApiModelProperty(value = "所属层，层属于后处理计算结果，实时性一般，初始值为65535，计算后会将其更新")
    private String layerIndex;
    private java.lang.String id;
    private java.lang.Integer isdj;
}
