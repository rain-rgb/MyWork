package com.trtm.iot.device_data.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 搅拌车监控
 * @Author: jeecg-boot
 * @Date:   2025-05-06
 * @Version: V1.0
 */
@Data
@TableName("device_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_data对象", description="搅拌车监控")
public class DeviceData implements Serializable {
    private static final long serialVersionUID = 1L;

	/**设备序列号，全局唯一*/
	@Excel(name = "设备序列号，全局唯一", width = 15)
    @ApiModelProperty(value = "设备序列号，全局唯一")
    private java.lang.String deviceid;
	/**gps设备上报的时间*/
	@Excel(name = "gps设备上报的时间", width = 15)
    @ApiModelProperty(value = "gps设备上报的时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date devicetime;
	/**到达服务器时间*/
	@Excel(name = "到达服务器时间", width = 15)
    @ApiModelProperty(value = "到达服务器时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date arrivedtime;
	/**综合计算后位置更新时间*/
	@Excel(name = "综合计算后位置更新时间", width = 15)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updatetime;
	/**最后位置定位时间*/
	@Excel(name = "最后位置定位时间", width = 15)
    @ApiModelProperty(value = "最后位置定位时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date validpoistiontime;
	/**计算出来的纬度*/
	@Excel(name = "计算出来的纬度", width = 15)
    @ApiModelProperty(value = "计算出来的纬度")
    private java.lang.Double callat;
	/**计算出来的经度*/
	@Excel(name = "计算出来的经度", width = 15)
    @ApiModelProperty(value = "计算出来的经度")
    private java.lang.Double callon;
	/**高度(米)*/
	@Excel(name = "高度(米)", width = 15)
    @ApiModelProperty(value = "高度(米)")
    private java.lang.Double altitude;
	/**定位精度米*/
	@Excel(name = "定位精度米", width = 15)
    @ApiModelProperty(value = "定位精度米")
    private java.lang.Integer radius;
	/**速度（米）*/
	@Excel(name = "速度（米）", width = 15)
    @ApiModelProperty(value = "速度（米）")
    private java.lang.Double speed;
	/**方向（0 - 360）*/
	@Excel(name = "方向（0 - 360）", width = 15)
    @ApiModelProperty(value = "方向（0 - 360）")
    private java.lang.Integer course;
	/**总里程米*/
	@Excel(name = "总里程米", width = 15)
    @ApiModelProperty(value = "总里程米")
    private java.lang.Integer totaldistance;
	/**总油量，单位毫升*/
	@Excel(name = "总油量，单位毫升", width = 15)
    @ApiModelProperty(value = "总油量，单位毫升")
    private java.lang.Integer totaloil;
	/**总非行驶油耗，单位毫升*/
	@Excel(name = "总非行驶油耗，单位毫升", width = 15)
    @ApiModelProperty(value = "总非行驶油耗，单位毫升")
    private java.lang.Integer totalnotrunningad;
	/**分开统计的时候第1个油箱，单位毫升*/
	@Excel(name = "分开统计的时候第1个油箱，单位毫升", width = 15)
    @ApiModelProperty(value = "分开统计的时候第1个油箱，单位毫升")
    private java.lang.Integer masteroil;
	/**分开统计的时候第2个油箱，单位毫升*/
	@Excel(name = "分开统计的时候第2个油箱，单位毫升", width = 15)
    @ApiModelProperty(value = "分开统计的时候第2个油箱，单位毫升")
    private java.lang.Integer auxoil;
	/**分开统计的时候第3个油箱，单位毫升*/
	@Excel(name = "分开统计的时候第3个油箱，单位毫升", width = 15)
    @ApiModelProperty(value = "分开统计的时候第3个油箱，单位毫升")
    private java.lang.Integer thirdoil;
	/**分开统计的时候第4个油箱，单位毫升*/
	@Excel(name = "分开统计的时候第4个油箱，单位毫升", width = 15)
    @ApiModelProperty(value = "分开统计的时候第4个油箱，单位毫升")
    private java.lang.Integer fourthoil;
	/**油量模拟量1*/
	@Excel(name = "油量模拟量1", width = 15)
    @ApiModelProperty(value = "油量模拟量1")
    private java.lang.Integer srcad0;
	/**油量模拟量2*/
	@Excel(name = "油量模拟量2", width = 15)
    @ApiModelProperty(value = "油量模拟量2")
    private java.lang.Integer srcad1;
	/**油量模拟量3*/
	@Excel(name = "油量模拟量3", width = 15)
    @ApiModelProperty(value = "油量模拟量3")
    private java.lang.Integer srcad2;
	/**油量模拟量4*/
	@Excel(name = "油量模拟量4", width = 15)
    @ApiModelProperty(value = "油量模拟量4")
    private java.lang.Integer srcad3;
	/**对应部标状态值*/
	@Excel(name = "对应部标状态值", width = 15)
    @ApiModelProperty(value = "对应部标状态值")
    private java.lang.Integer status;
	/**对应部标状态文字描述*/
	@Excel(name = "对应部标状态文字描述", width = 15)
    @ApiModelProperty(value = "对应部标状态文字描述")
    private java.lang.String strstatus;
	/**对应部标状态英文文字描述*/
	@Excel(name = "对应部标状态英文文字描述", width = 15)
    @ApiModelProperty(value = "对应部标状态英文文字描述")
    private java.lang.String strstatusen;
	/**对应部标报警值*/
	@Excel(name = "对应部标报警值", width = 15)
    @ApiModelProperty(value = "对应部标报警值")
    private java.lang.Integer alarm;
	/**对应部标报警文字描述*/
	@Excel(name = "对应部标报警文字描述", width = 15)
    @ApiModelProperty(value = "对应部标报警文字描述")
    private java.lang.String stralarm;
	/**对应部标报警英文文字描述*/
	@Excel(name = "对应部标报警英文文字描述", width = 15)
    @ApiModelProperty(value = "对应部标报警英文文字描述")
    private java.lang.String stralarmsen;
	/**对应部标视频报警值*/
	@Excel(name = "对应部标视频报警值", width = 15)
    @ApiModelProperty(value = "对应部标视频报警值")
    private java.lang.Integer videoalarm;
	/**对应部标视频报警文字描述*/
	@Excel(name = "对应部标视频报警文字描述", width = 15)
    @ApiModelProperty(value = "对应部标视频报警文字描述")
    private java.lang.String strvideoalarm;
	/**对应部标视频报警英文文字描述*/
	@Excel(name = "对应部标视频报警英文文字描述", width = 15)
    @ApiModelProperty(value = "对应部标视频报警英文文字描述")
    private java.lang.String strvideoalarmen;
	/**视频信号丢失状态*/
	@Excel(name = "视频信号丢失状态", width = 15)
    @ApiModelProperty(value = "视频信号丢失状态")
    private java.lang.Integer videosignalloststatus;
	/**视频遮挡状态*/
	@Excel(name = "视频遮挡状态", width = 15)
    @ApiModelProperty(value = "视频遮挡状态")
    private java.lang.Integer videosignalcoverstatus;
	/**视频异常驾驶行为状态*/
	@Excel(name = "视频异常驾驶行为状态", width = 15)
    @ApiModelProperty(value = "视频异常驾驶行为状态")
    private java.lang.Integer videobehavior;
	/**视频疲劳程度*/
	@Excel(name = "视频疲劳程度", width = 15)
    @ApiModelProperty(value = "视频疲劳程度")
    private java.lang.Integer videofatiguedegree;
	/**位置信息来源定位类型 (gps,wifi,cell)*/
	@Excel(name = "位置信息来源定位类型 (gps,wifi,cell)", width = 15)
    @ApiModelProperty(value = "位置信息来源定位类型 (gps,wifi,cell)")
    private java.lang.String gotsrc;
	/**基站信号强度*/
	@Excel(name = "基站信号强度", width = 15)
    @ApiModelProperty(value = "基站信号强度")
    private java.lang.Integer rxlevel;
	/**gps有效数*/
	@Excel(name = "gps有效数", width = 15)
    @ApiModelProperty(value = "gps有效数")
    private java.lang.Integer gpsvalidnum;
	/**外部电压，单位0.01V 默认是-1,0代表断电了*/
	@Excel(name = "外部电压，单位0.01V 默认是-1,0代表断电了", width = 15)
    @ApiModelProperty(value = "外部电压，单位0.01V 默认是-1,0代表断电了")
    private java.lang.Double exvoltage;
	/**电压，单位0.01V 默认是-1,0代表断电了*/
	@Excel(name = "电压，单位0.01V 默认是-1,0代表断电了", width = 15)
    @ApiModelProperty(value = "电压，单位0.01V 默认是-1,0代表断电了")
    private java.lang.Double voltagev;
	/**电量百分比，有的设备百分比是通过上报次数计算百分比*/
	@Excel(name = "电量百分比，有的设备百分比是通过上报次数计算百分比", width = 15)
    @ApiModelProperty(value = "电量百分比，有的设备百分比是通过上报次数计算百分比")
    private java.lang.Double voltagepercent;
	/**是否移动，是否在运动 0代表没运动 1代表在运动*/
	@Excel(name = "是否移动，是否在运动 0代表没运动 1代表在运动", width = 15)
    @ApiModelProperty(value = "是否移动，是否在运动 0代表没运动 1代表在运动")
    private java.lang.Integer moving;
	/**最后停留的lat*/
	@Excel(name = "最后停留的lat", width = 15)
    @ApiModelProperty(value = "最后停留的lat")
    private java.lang.Double parklat;
	/**最后停留的lon*/
	@Excel(name = "最后停留的lon", width = 15)
    @ApiModelProperty(value = "最后停留的lon")
    private java.lang.Double parklon;
	/**最后停留时间*/
	@Excel(name = "最后停留时间", width = 15)
    @ApiModelProperty(value = "最后停留时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date parktime;
	/**停留了多久*/
	@Excel(name = "停留了多久", width = 15)
    @ApiModelProperty(value = "停留了多久")
    private java.lang.Integer parkduration;
	/**温度1，单位 1/100度， 65535 代表无效*/
	@Excel(name = "温度1，单位 1/100度， 65535 代表无效", width = 15)
    @ApiModelProperty(value = "温度1，单位 1/100度， 65535 代表无效")
    private java.lang.Integer temp1;
	/**温度2，单位 1/100度， 65535 代表无效*/
	@Excel(name = "温度2，单位 1/100度， 65535 代表无效", width = 15)
    @ApiModelProperty(value = "温度2，单位 1/100度， 65535 代表无效")
    private java.lang.Integer temp2;
	/**温度3，单位 1/100度， 65535 代表无效*/
	@Excel(name = "温度3，单位 1/100度， 65535 代表无效", width = 15)
    @ApiModelProperty(value = "温度3，单位 1/100度， 65535 代表无效")
    private java.lang.Integer temp3;
	/**温度4，单位 1/100度， 65535 代表无效*/
	@Excel(name = "温度4，单位 1/100度， 65535 代表无效", width = 15)
    @ApiModelProperty(value = "温度4，单位 1/100度， 65535 代表无效")
    private java.lang.Integer temp4;
	/**湿度1*/
	@Excel(name = "湿度1", width = 15)
    @ApiModelProperty(value = "湿度1")
    private java.lang.Integer humi1;
	/**湿度2*/
	@Excel(name = "湿度2", width = 15)
    @ApiModelProperty(value = "湿度2")
    private java.lang.Integer humi2;
	/**IO 状态位*/
	@Excel(name = "IO 状态位", width = 15)
    @ApiModelProperty(value = "IO 状态位")
    private java.lang.Integer iostatus;
	/**1代表正在超速 2代表未超速状态*/
	@Excel(name = "1代表正在超速 2代表未超速状态", width = 15)
    @ApiModelProperty(value = "1代表正在超速 2代表未超速状态")
    private java.lang.Integer currentoverspeedstate;
	/**正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)*/
	@Excel(name = "正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)", width = 15)
    @ApiModelProperty(value = "正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)")
    private java.lang.Integer rotatestatus;
	/**载重状态 0x00：空车；0x01：半载；0x02：超载；0x03：满载 0x04 装载 0x05 卸载*/
	@Excel(name = "载重状态 0x00：空车；0x01：半载；0x02：超载；0x03：满载 0x04 装载 0x05 卸载", width = 15)
    @ApiModelProperty(value = "载重状态 0x00：空车；0x01：半载；0x02：超载；0x03：满载 0x04 装载 0x05 卸载")
    private java.lang.Integer loadstatus;
	/**重量 单位0.1kg*/
	@Excel(name = "重量 单位0.1kg", width = 15)
    @ApiModelProperty(value = "重量 单位0.1kg")
    private java.lang.Integer weight;
	/**载重里面的ad0,目前只做1路*/
	@Excel(name = "载重里面的ad0,目前只做1路", width = 15)
    @ApiModelProperty(value = "载重里面的ad0,目前只做1路")
    private java.lang.Integer srcweightad0;
	/**上报类型*/
	@Excel(name = "上报类型", width = 15)
    @ApiModelProperty(value = "上报类型")
    private java.lang.Integer reportmode;
}
