package com.trtm.iot.devicemixgrouted.entity;

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
 * @Description: device_mixpile_grouted_real
 * @Author: jeecg-boot
 * @Date:   2024-04-30
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_grouted_real")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_grouted_real对象", description="device_mixpile_grouted_real")
public class DeviceMixpileGroutedReal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String sbbh;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String pileMileage;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileNo;
	/**设计深度*/
	@Excel(name = "设计深度", width = 15)
    @ApiModelProperty(value = "设计深度")
    private java.lang.String pileDesigndep;
	/**实际灌浆深度*/
	@Excel(name = "实际灌浆深度", width = 15)
    @ApiModelProperty(value = "实际灌浆深度")
    private java.lang.String pileRealdep;
	/**灌浆时长*/
	@Excel(name = "灌浆时长", width = 15)
    @ApiModelProperty(value = "灌浆时长")
    private java.lang.String pileWorktime;
	/**成桩时间*/
	@Excel(name = "成桩时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "成桩时间")
    private java.util.Date pileTime;
	/**密度（水泥浆比重）*/
	@Excel(name = "密度（水泥浆比重）", width = 15)
    @ApiModelProperty(value = "密度（水泥浆比重）")
    private java.lang.String pileDensity;
//	/**处置状态*/
//	@Excel(name = "处置状态", width = 15)
//    @ApiModelProperty(value = "处置状态")
//    private java.lang.String handlestate;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date pileStarttime;
	/**alertstate*/
	@Excel(name = "alertstate", width = 15)
    @ApiModelProperty(value = "alertstate")
    private java.lang.String alertstate;
	/**超标 0合格，1不合格*/
	@Excel(name = "状态 0未注浆，1注浆中", width = 15)
    @ApiModelProperty(value = "超标 0未注浆，1注浆中")
    private java.lang.String chaobiaodengji;
	/**超标原因*/
	@Excel(name = "超标原因", width = 15)
    @ApiModelProperty(value = "超标原因")
    private java.lang.String problem;
	/**数据上传时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date createTime;
	/**倾角-x*/
	@Excel(name = "倾角-x", width = 15)
    @ApiModelProperty(value = "倾角-x")
    private java.lang.String pileX;
	/**倾角-y*/
	@Excel(name = "倾角-y", width = 15)
    @ApiModelProperty(value = "倾角-y")
    private java.lang.String pileY;
	/**桩经度*/
	@Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private java.lang.String pileLgd;
	/**桩经度*/
	@Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private java.lang.String pileLtd;
	/**ts*/
	@Excel(name = "ts", width = 15)
    @ApiModelProperty(value = "ts")
    private java.lang.String ts;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String uuid;
	/**软基任务单号*/
	@Excel(name = "软基任务单号", width = 15)
    @ApiModelProperty(value = "软基任务单号")
    private java.lang.String mixrwd;
	/**灌浆开始时间*/
	@Excel(name = "灌浆开始时间", width = 15)
    @ApiModelProperty(value = "灌浆开始时间")
    private java.lang.String gjstarttime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  java.util.Date updateTime;
    private String pileSpeed;// 速度
    private String pileElectr;// 电流
    private String grouting;// 累计浆量（L）
    private String ratio;// 水灰比
    private String serial;// 往返次数
    private String press;// 管浆压力
    private String flowmeter;// 平均流量(L/min)
    private String serialtype;//状态 0 停止；1下钻；2上钻

}
