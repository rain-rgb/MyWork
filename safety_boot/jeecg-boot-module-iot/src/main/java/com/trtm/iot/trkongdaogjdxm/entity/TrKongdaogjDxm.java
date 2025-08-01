package com.trtm.iot.trkongdaogjdxm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 孔道灌浆（定性检测）主表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@ApiModel(value="tr_kongdaogj_dxm对象", description="孔道灌浆（定性检测）主表")
@Data
@TableName("tr_kongdaogj_dxm")
public class TrKongdaogjDxm implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
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
	@Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
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
	@Excel(name = "检测时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	@Dict(dicCode = "triggerchannel")
	private java.lang.Integer triggerchannel;
	/**触发电平*/
	@Excel(name = "触发电平", width = 15)
	@ApiModelProperty(value = "触发电平")
	private java.lang.Integer triggerlevel;
	/**测试模式（0：无线 1：有线）*/
	@Excel(name = "测试模式（0：无线 1：有线）", width = 15)
	@ApiModelProperty(value = "测试模式（0：无线 1：有线）")
	@Dict(dicCode = "testmode")
	private java.lang.Integer testmode;
	/**GPS 状态（0：无效 1：有效）*/
	@Excel(name = "GPS 状态（0：无效 1：有效）", width = 15)
	@ApiModelProperty(value = "GPS 状态（0：无效 1：有效）")
	@Dict(dicCode = "gpsvalid")
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
	/**孔道高差(单位：m)*/
	@Excel(name = "孔道高差(单位：m)", width = 15)
    @ApiModelProperty(value = "孔道高差(单位：m)")
    private java.math.BigDecimal holede;
	/**锚索波速(m/s)*/
	@Excel(name = "锚索波速(m/s)", width = 15)
    @ApiModelProperty(value = "锚索波速(m/s)")
    private java.lang.Integer anchorropev;
	/**传感器 1 到梁板长度(单位m)*/
	@Excel(name = "传感器 1 到梁板长度(单位m)", width = 15)
    @ApiModelProperty(value = "传感器 1 到梁板长度(单位m)")
    private java.math.BigDecimal ch1length;
	/**传感器 2 到梁板长度(单位m)*/
	@Excel(name = "传感器 2 到梁板长度(单位m)", width = 15)
    @ApiModelProperty(value = "传感器 2 到梁板长度(单位m)")
    private java.math.BigDecimal ch2length;
}
