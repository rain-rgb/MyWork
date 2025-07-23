package com.trtm.iot.device_mixer_trucks.entity;

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
 * @Description: 搅拌车监控
 * @Author: jeecg-boot
 * @Date:   2025-05-06
 * @Version: V1.0
 */
@Data
@TableName("device_mixer_trucks")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixer_trucks对象", description="搅拌车监控")
public class DeviceMixerTrucks implements Serializable {
    private static final long serialVersionUID = 1L;

    /**设备名称
     */
    @Excel(name = "设备名称 ", width = 15)
    @ApiModelProperty(value = "设备名称 ")
    private java.lang.String devicename;
    /**设备id*/
    @Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private java.lang.String deviceid;
    /**wgs84格式的纬度*/
    @Excel(name = "wgs84格式的纬度", width = 15)
    @ApiModelProperty(value = "wgs84格式的纬度")
    private java.lang.Double lat;
    /**wgs84格式的经度*/
    @Excel(name = "wgs84格式的经度", width = 15)
    @ApiModelProperty(value = "wgs84格式的经度")
    private java.lang.Double lon;
    /**到达时间 utc 时间*/
    @Excel(name = "到达时间 utc 时间", width = 15)
    @ApiModelProperty(value = "到达时间 utc 时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updatetime;
    /**最后有效的定位时间*/
    @Excel(name = "最后有效的定位时间", width = 15)
    @ApiModelProperty(value = "最后有效的定位时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date validpoistiontime;
    /**定位半径 单位米*/
    @Excel(name = "定位半径 单位米", width = 15)
    @ApiModelProperty(value = "定位半径 单位米")
    private java.lang.Double radius;
    /**速度 单位米*/
    @Excel(name = "速度 单位米", width = 15)
    @ApiModelProperty(value = "速度 单位米")
    private java.lang.Double speed;
    /**高度 米*/
    @Excel(name = "高度 米", width = 15)
    @ApiModelProperty(value = "高度 米")
    private java.lang.Double altitude;
    /**角度 方向 0~360度*/
    @Excel(name = "角度 方向 0~360度", width = 15)
    @ApiModelProperty(value = "角度 方向 0~360度")
    private java.lang.Integer course;
    /**服务器计算的总里程米 the server calculate the total distance*/
    @Excel(name = "服务器计算的总里程米 the server calculate the total distance", width = 15)
    @ApiModelProperty(value = "服务器计算的总里程米 the server calculate the total distance")
    private java.lang.Integer totaldistance;
    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**状态描述*/
    @Excel(name = "状态描述", width = 15)
    @ApiModelProperty(value = "状态描述")
    private java.lang.String strstatus;
    /**报警
     */
    @Excel(name = "报警 ", width = 15)
    @ApiModelProperty(value = "报警 ")
    private java.lang.Integer alarm;
    /**报警描述*/
    @Excel(name = "报警描述", width = 15)
    @ApiModelProperty(value = "报警描述")
    private java.lang.String stralarm;
    /**cell wifi gps 定位类型*/
    @Excel(name = "cell wifi gps 定位类型", width = 15)
    @ApiModelProperty(value = "cell wifi gps 定位类型")
    private java.lang.String gotsrc;
    /**接收信号强度 百分比 10 40 60 80 100 这个固定值*/
    @Excel(name = "接收信号强度 百分比 10 40 60 80 100 这个固定值", width = 15)
    @ApiModelProperty(value = "接收信号强度 百分比 10 40 60 80 100 这个固定值")
    private java.lang.Integer rxlevel;
    /**电压V，有的设备没有电压*/
    @Excel(name = "电压V，有的设备没有电压", width = 15)
    @ApiModelProperty(value = "电压V，有的设备没有电压")
    private java.lang.Double voltagev;
    /**电量百分比，有的设备百分比是通过上报次数计算百分比*/
    @Excel(name = "电量百分比，有的设备百分比是通过上报次数计算百分比", width = 15)
    @ApiModelProperty(value = "电量百分比，有的设备百分比是通过上报次数计算百分比")
    private java.lang.Integer voltagepercent;
    /**卫星个数*/
    @Excel(name = "卫星个数", width = 15)
    @ApiModelProperty(value = "卫星个数")
    private java.lang.Integer gpsvalidnum;
    /**IO 状态位*/
    @Excel(name = "IO 状态位", width = 15)
    @ApiModelProperty(value = "IO 状态位")
    private java.lang.Integer iostatus;
    /**0x52	1	正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)*/
    @Excel(name = "0x52	1	正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)", width = 15)
    @ApiModelProperty(value = "0x52	1	正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)")
    private java.lang.Integer rotatestatus;
    /**reportmode*/
    @Excel(name = "reportmode", width = 15)
    @ApiModelProperty(value = "reportmode")
    private java.lang.Integer reportmode;
    /**是否在运动 0代表没运动 1代表在运动 判断依据当前位置跟上一次的位置是否超过1米*/
    @Excel(name = "是否在运动 0代表没运动 1代表在运动 判断依据当前位置跟上一次的位置是否超过1米", width = 15)
    @ApiModelProperty(value = "是否在运动 0代表没运动 1代表在运动 判断依据当前位置跟上一次的位置是否超过1米")
    private java.lang.Integer moving;
    /**停留了多久,单位utc时间*/
    @Excel(name = "停留了多久,单位utc时间", width = 15)
    @ApiModelProperty(value = "停留了多久,单位utc时间")
    private java.lang.String parkduration;
}
