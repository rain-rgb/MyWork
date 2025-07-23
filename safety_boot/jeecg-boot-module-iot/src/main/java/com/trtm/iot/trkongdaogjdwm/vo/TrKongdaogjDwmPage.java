package com.trtm.iot.trkongdaogjdwm.vo;

import java.util.List;
import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDwm;
import com.trtm.iot.trkongdaogjdwm.entity.TrKongdaogjDws;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 孔道灌浆（定位检测）主表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="tr_kongdaogj_dwmPage对象", description="孔道灌浆（定位检测）主表")
public class TrKongdaogjDwmPage {

	/**id*/
	@ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**设备商标识*/
	@Excel(name = "设备商标识", width = 15)
	@ApiModelProperty(value = "设备商标识")
	private java.lang.String vendorid;
	/**流水号 */
	@Excel(name = "流水号 ", width = 15)
	@ApiModelProperty(value = "流水号 ")
	private java.lang.String serialno;
	/**测试仪编号*/
	@Excel(name = "测试仪编号", width = 15)
	@ApiModelProperty(value = "测试仪编号")
	private java.lang.String machineid;
	/**孔道编号*/
	@Excel(name = "孔道编号", width = 15)
	@ApiModelProperty(value = "孔道编号")
	private java.lang.String holeno;
	/**梁板名称*/
	@Excel(name = "梁板名称", width = 15)
	@ApiModelProperty(value = "梁板名称")
	private java.lang.String beamname;
	/**检测日期*/
	@Excel(name = "检测日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "检测日期")
	private java.util.Date testdate;
	/**检测时间*/
	@Excel(name = "检测时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "检测时间")
	private java.util.Date testtime;
	/**检测人员*/
	@Excel(name = "检测人员", width = 15)
	@ApiModelProperty(value = "检测人员")
	private java.lang.String worker;
	/**检测单位*/
	@Excel(name = "检测单位", width = 15)
	@ApiModelProperty(value = "检测单位")
	private java.lang.String testunit;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
	@ApiModelProperty(value = "施工单位")
	private java.lang.String constructionunit;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
	@ApiModelProperty(value = "工程名称")
	private java.lang.String projectname;
	/**梁板长度 */
	@Excel(name = "梁板长度 ", width = 15)
	@ApiModelProperty(value = "梁板长度 ")
	private java.math.BigDecimal beamlength;
	/**混凝土波速*/
	@Excel(name = "混凝土波速", width = 15)
	@ApiModelProperty(value = "混凝土波速")
	private java.lang.Integer concretev;
	/**采样间隔 （单位 μs）*/
	@Excel(name = "采样间隔 （单位 μs）", width = 15)
	@ApiModelProperty(value = "采样间隔 （单位 μs）")
	private java.math.BigDecimal sampleinterval;
	/**低通（单位kHz，3kHz、5kHz、10kHz……全通若值为 99999 表示全通）*/
	@Excel(name = "低通（单位kHz，3kHz、5kHz、10kHz……全通若值为 99999 表示全通）", width = 15)
	@ApiModelProperty(value = "低通（单位kHz，3kHz、5kHz、10kHz……全通若值为 99999 表示全通）")
	private java.lang.Integer lowfilter;
	/**高通 （单位 Hz，0Hz、50Hz、100Hz、200Hz…）*/
	@Excel(name = "高通 （单位 Hz，0Hz、50Hz、100Hz、200Hz…）", width = 15)
	@ApiModelProperty(value = "高通 （单位 Hz，0Hz、50Hz、100Hz、200Hz…）")
	private java.lang.Integer highfilter;
	/**采样长度*/
	@Excel(name = "采样长度", width = 15)
	@ApiModelProperty(value = "采样长度")
	private java.lang.Integer samplelength;
	/**延迟点数*/
	@Excel(name = "延迟点数", width = 15)
	@ApiModelProperty(value = "延迟点数")
	private java.lang.Integer delaydot;
	/**触发通道（0:任意 1:CH1 2:CH2）*/
	@Excel(name = "触发通道（0:任意 1:CH1 2:CH2）", width = 15)
	@ApiModelProperty(value = "触发通道（0:任意 1:CH1 2:CH2）")
	private java.lang.Integer triggerchannel;
	/**触发电平*/
	@Excel(name = "触发电平", width = 15)
	@ApiModelProperty(value = "触发电平")
	private java.lang.Integer triggerlevel;
	/**测试模式（0：无线 1：有线
	 ）*/
	@Excel(name = "测试模式（0：无线 1：有线）", width = 15)
	@ApiModelProperty(value = "测试模式（0：无线 1：有线）")
	private java.lang.Integer testmode;
	/**GPS 状态（0：无效 1：有效）*/
	@Excel(name = "GPS 状态（0：无效 1：有效）", width = 15)
	@ApiModelProperty(value = "GPS 状态（0：无效 1：有效）")
	private java.lang.Integer gpsvalid;
	/**经度 */
	@Excel(name = "经度 ", width = 15)
	@ApiModelProperty(value = "经度 ")
	private java.math.BigDecimal gpslongitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
	@ApiModelProperty(value = "纬度")
	private java.math.BigDecimal gpslatitude;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remarks;
	/**梁板厚度 （单位：m）*/
	@Excel(name = "梁板厚度 （单位：m）", width = 15)
	@ApiModelProperty(value = "梁板厚度 （单位：m）")
	private java.math.BigDecimal beamthickness;
	/**始测点位置 （单位：m）*/
	@Excel(name = "始测点位置 （单位：m）", width = 15)
	@ApiModelProperty(value = "始测点位置 （单位：m）")
	private java.math.BigDecimal initpos;
	/**测点间距  （单位：m）*/
	@Excel(name = "测点间距  （单位：m）", width = 15)
	@ApiModelProperty(value = "测点间距  （单位：m）")
	private java.math.BigDecimal pointspace;
	/**通道号*/
	@Excel(name = "通道号", width = 15)
	@ApiModelProperty(value = "通道号")
	private java.lang.Integer channelnum;
	/**传感器类型 （0:加速度计1:速度计）*/
	@Excel(name = "传感器类型 （0:加速度计1:速度计）", width = 15)
	@ApiModelProperty(value = "传感器类型 （0:加速度计1:速度计）")
	private java.lang.Integer sensortype;
	/**传感器灵敏度系数 （探头类型为加速度计,单位 mv/g ; 探头类型 为 速 度 计 , 单 位mv/(m/s)）*/
	@Excel(name = "传感器灵敏度系数 （探头类型为加速度计,单位 mv/g ; 探头类型 为 速 度 计 , 单 位mv/(m/s)）", width = 15)
	@ApiModelProperty(value = "传感器灵敏度系数 （探头类型为加速度计,单位 mv/g ; 探头类型 为 速 度 计 , 单 位mv/(m/s)）")
	private java.lang.Integer sensorsensitive;
	/**积分状态（是否积分, 1:积分 0：不积分）*/
	@Excel(name = "积分状态（是否积分, 1:积分 0：不积分）", width = 15)
	@ApiModelProperty(value = "积分状态（是否积分, 1:积分 0：不积分）")
	private java.lang.Integer integralflag;

	@ExcelCollection(name="孔道灌浆（定位检测）子表")
	@ApiModelProperty(value = "孔道灌浆（定位检测）子表")
	private List<TrKongdaogjDws> trKongdaogjDwsList;

}
