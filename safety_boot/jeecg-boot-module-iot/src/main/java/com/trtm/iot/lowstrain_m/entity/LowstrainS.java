package com.trtm.iot.lowstrain_m.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 低应变子表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
@ApiModel(value="lowstrain_s对象", description="低应变子表")
@Data
@TableName("lowstrain_s")
public class LowstrainS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**主表id*/
    @ApiModelProperty(value = "主表id")
    private java.lang.String baseinfoid;
	/**序列号*/
	@Excel(name = "序列号", width = 15)
    @ApiModelProperty(value = "序列号")
    private java.lang.Integer seq;
	/**采样点的点号*/
	@Excel(name = "采样点的点号", width = 15)
    @ApiModelProperty(value = "采样点的点号")
    private java.lang.Integer pointno;
	/**当前锤次*/
	@Excel(name = "当前锤次", width = 15)
    @ApiModelProperty(value = "当前锤次")
    private java.lang.Integer curblowindex;
	/**采样时间*/
	@Excel(name = "采样时间", width = 15)
    @ApiModelProperty(value = "采样时间")
    private java.lang.String sampletime;
	/**本道信号的增益值*/
	@Excel(name = "本道信号的增益值", width = 15)
    @ApiModelProperty(value = "本道信号的增益值")
    private java.lang.Integer nchngain;
	/**滤波方式（-1-无，0－低通，1－高通，2－带通）*/
	@Excel(name = "滤波方式（-1-无，0－低通，1－高通，2－带通）", width = 15)
    @ApiModelProperty(value = "滤波方式（-1-无，0－低通，1－高通，2－带通）")
    private java.lang.Integer fitertype;
	/**高通滤波（Hz）*/
	@Excel(name = "高通滤波（Hz）", width = 15)
    @ApiModelProperty(value = "高通滤波（Hz）")
    private java.math.BigDecimal highpass;
	/**低通滤波（Hz）*/
	@Excel(name = "低通滤波（Hz）", width = 15)
    @ApiModelProperty(value = "低通滤波（Hz）")
    private java.math.BigDecimal lowpass;
	/**是否积分（0：积分（加速度计）； 1：不积分（速度计））*/
	@Excel(name = "是否积分（0：积分（加速度计）； 1：不积分（速度计））", width = 15)
    @ApiModelProperty(value = "是否积分（0：积分（加速度计）； 1：不积分（速度计））")
	private java.lang.Integer integralflag;
	/**平滑点数*/
	@Excel(name = "平滑点数", width = 15)
    @ApiModelProperty(value = "平滑点数")
    private java.lang.Integer smooth;
	/**放大类型 （0-指数放大  1-线性放大）*/
	@Excel(name = "放大类型 （0-指数放大  1-线性放大）", width = 15)
    @ApiModelProperty(value = "放大类型 （0-指数放大  1-线性放大）")
	private java.lang.Integer magtype;
	/**放大系数*/
	@Excel(name = "放大系数", width = 15)
    @ApiModelProperty(value = "放大系数")
    private java.lang.Integer magfactor;
	/**放大起点（m）*/
	@Excel(name = "放大起点（m）", width = 15)
    @ApiModelProperty(value = "放大起点（m）")
    private java.math.BigDecimal magstart;
	@Excel(name = "旋转百分比", width = 15)
    @ApiModelProperty(value = "旋转百分比")
    private java.math.BigDecimal rotation;
	/**小波因子*/
	@Excel(name = "小波因子", width = 15)
    @ApiModelProperty(value = "小波因子")
    private java.math.BigDecimal waveletscale;
	/**探头类型（0：加速度计；  1：速度计）*/
	@Excel(name = "探头类型（0：加速度计；  1：速度计）", width = 15)
    @ApiModelProperty(value = "探头类型（0：加速度计；  1：速度计）")
    private java.lang.Integer sensortype;
	/**采样间隔（us）*/
	@Excel(name = "采样间隔（us）", width = 15)
    @ApiModelProperty(value = "采样间隔（us）")
    private java.math.BigDecimal samplinginterval;
	/**传感器系数 探头类型为加速度计 单位: mv/(m/s2)
探头类型为速度计 单位: mv/(m/s)*/
	@Excel(name = "传感器系数 探头类型为加速度计 单位: mv/(m/s2) 探头类型为速度计 单位: mv/(m/s)", width = 15)
    @ApiModelProperty(value = "传感器系数 探头类型为加速度计 单位: mv/(m/s2) 探头类型为速度计 单位: mv/(m/s)")
    private java.math.BigDecimal sensitivity;
	/**桩顶位置*/
	@Excel(name = "桩顶位置", width = 15)
    @ApiModelProperty(value = "桩顶位置")
    private java.lang.Integer piletop;
	/**桩底位置*/
	@Excel(name = "桩底位置", width = 15)
    @ApiModelProperty(value = "桩底位置")
    private java.lang.Integer pilebot;
	/**延迟点数*/
	@Excel(name = "延迟点数", width = 15)
    @ApiModelProperty(value = "延迟点数")
    private java.lang.Integer delaypoints;
	/**采样点数*/
	@Excel(name = "采样点数", width = 15)
    @ApiModelProperty(value = "采样点数")
    private java.lang.Integer samplepoints;
	/**最大信号百分比*/
	@Excel(name = "最大信号百分比", width = 15)
    @ApiModelProperty(value = "最大信号百分比")
    private java.math.BigDecimal maxdatapercent;
	/**原始波形数值*/
	@Excel(name = "原始波形数值", width = 15)
    @ApiModelProperty(value = "原始波形数值")
    private java.lang.String orgwavedata;
	/**处理后波形数值*/
	@Excel(name = "处理后波形数值", width = 15)
    @ApiModelProperty(value = "处理后波形数值")
    private java.lang.String wavedata;
}
