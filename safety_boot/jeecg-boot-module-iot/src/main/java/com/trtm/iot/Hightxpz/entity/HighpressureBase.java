package com.trtm.iot.Hightxpz.entity;

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
 * @Description: 高压旋喷桩主表
 * @Author: jeecg-boot
 * @Date:   2021-04-16
 * @Version: V1.0
 */
@Data
@TableName("highpressure_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="highpressure_base对象", description="高压旋喷桩主表")
public class HighpressureBase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编码*/
	@Excel(name = "设备编码", width = 15)
    @ApiModelProperty(value = "设备编码")
    private java.lang.String equipmentcode;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String constructionstakenum;
	/**客户端当前记录产生时间（实施采集时间）*/
	@Excel(name = "客户端当前记录产生时间（实施采集时间）", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "客户端当前记录产生时间（实施采集时间）")
    private java.util.Date realtime;
	/**开始时间（该根桩的开始时间）*/
	@Excel(name = "开始时间（该根桩的开始时间）", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间（该根桩的开始时间）")
    private java.util.Date starttime;
	/**记录状态：1-开始，2-暂停，3-结束,4-报警；*/
	@Excel(name = "记录状态：1-开始，2-暂停，3-结束,4-报警；", width = 15)
    @ApiModelProperty(value = "记录状态：1-开始，2-暂停，3-结束,4-报警；")
    private java.lang.Integer recordstatus;
	/**钻杆状态：1-下钻，2-提钻，3-复下，4-复提；*/
	@Excel(name = "钻杆状态：1-下钻，2-提钻，3-复下，4-复提；", width = 15)
    @ApiModelProperty(value = "钻杆状态：1-下钻，2-提钻，3-复下，4-复提；")
    private java.lang.Integer pipestatus;
	/**喷浆状态：1-喷浆，0-止喷*/
	@Excel(name = "喷浆状态：1-喷浆，0-止喷", width = 15)
    @ApiModelProperty(value = "喷浆状态：1-喷浆，0-止喷")
    private java.lang.Integer nozzlestatus;
	/**喷射模式：1-单管法，6-二重管法，3-三重管法*/
	@Excel(name = "喷射模式：1-单管法，6-二重管法，3-三重管法", width = 15)
    @ApiModelProperty(value = "喷射模式：1-单管法，6-二重管法，3-三重管法")
    private java.lang.Integer pumpmode;
	/**记录模式：0-深度模式，1-时间模式*/
	@Excel(name = "记录模式：0-深度模式，1-时间模式", width = 15)
    @ApiModelProperty(value = "记录模式：0-深度模式，1-时间模式")
    private java.lang.Integer recordmode;
	/**钻杆模式：0-单杆模式，1-接杆模式*/
	@Excel(name = "钻杆模式：0-单杆模式，1-接杆模式", width = 15)
    @ApiModelProperty(value = "钻杆模式：0-单杆模式，1-接杆模式")
    private java.lang.Integer pipemode;
	/**水灰比*/
	@Excel(name = "水灰比", width = 15)
    @ApiModelProperty(value = "水灰比")
    private java.lang.Double waterashratio;
	/**记录间隔：间隔的数值，深度对应单位 cm，时间对应单位 s*/
	@Excel(name = "记录间隔：间隔的数值，深度对应单位 cm，时间对应单位 s", width = 15)
    @ApiModelProperty(value = "记录间隔：间隔的数值，深度对应单位 cm，时间对应单位 s")
    private java.lang.Integer recordinterval;
	/**段浆量(L)*/
	@Excel(name = "段浆量(L)", width = 15)
    @ApiModelProperty(value = "段浆量(L)")
    private java.lang.Double segmentpulp;
	/**段灰量(kg)*/
	@Excel(name = "段灰量(kg)", width = 15)
    @ApiModelProperty(value = "段灰量(kg)")
    private java.lang.Double segmentash;
	/**累计浆量（L）*/
	@Excel(name = "累计浆量（L）", width = 15)
    @ApiModelProperty(value = "累计浆量（L）")
    private java.lang.Double totalpulp;
	/**累计灰量（KG）*/
	@Excel(name = "累计灰量（KG）", width = 15)
    @ApiModelProperty(value = "累计灰量（KG）")
    private java.lang.Double totalash;
	/**累计水量(L)*/
	@Excel(name = "累计水量(L)", width = 15)
    @ApiModelProperty(value = "累计水量(L)")
    private java.lang.Double totalwater;
	/**累计气量(m3)*/
	@Excel(name = "累计气量(m3)", width = 15)
    @ApiModelProperty(value = "累计气量(m3)")
    private java.lang.Double totalair;
	/**累计返浆量(L)*/
	@Excel(name = "累计返浆量(L)", width = 15)
    @ApiModelProperty(value = "累计返浆量(L)")
    private java.lang.Double totalreturnpulp;
	/**水喷压(MPa)*/
	@Excel(name = "水喷压(MPa)", width = 15)
    @ApiModelProperty(value = "水喷压(MPa)")
    private java.lang.Double waterpressure;
	/**水流量(L)*/
	@Excel(name = "水流量(L)", width = 15)
    @ApiModelProperty(value = "水流量(L)")
    private java.lang.Double waterflow;
	/**气流量(m3)*/
	@Excel(name = "气流量(m3)", width = 15)
    @ApiModelProperty(value = "气流量(m3)")
    private java.lang.Double airflow;
	/**气喷压(MPa)*/
	@Excel(name = "气喷压(MPa)", width = 15)
    @ApiModelProperty(value = "气喷压(MPa)")
    private java.lang.Double airpressure;
	/**返浆量(L)*/
	@Excel(name = "返浆量(L)", width = 15)
    @ApiModelProperty(value = "返浆量(L)")
    private java.lang.Double returnpulp;
	/**返浆压力(MPa)*/
	@Excel(name = "返浆压力(MPa)", width = 15)
    @ApiModelProperty(value = "返浆压力(MPa)")
    private java.lang.Double returnpulppressure;
	/**密度(g/cm3)*/
	@Excel(name = "密度(g/cm3)", width = 15)
    @ApiModelProperty(value = "密度(g/cm3)")
    private java.lang.Double density;
	/**浆喷压(MPa)*/
	@Excel(name = "浆喷压(MPa)", width = 15)
    @ApiModelProperty(value = "浆喷压(MPa)")
    private java.lang.Double pressure;
	/**下钻/提钻速度(cm/min)*/
	@Excel(name = "下钻/提钻速度(cm/min)", width = 15)
    @ApiModelProperty(value = "下钻/提钻速度(cm/min)")
    private java.lang.Double speed;
	/**钻速(r/min)*/
	@Excel(name = "钻速(r/min)", width = 15)
    @ApiModelProperty(value = "钻速(r/min)")
    private java.lang.Double drillingrate;
	/**深度(m)*/
	@Excel(name = "深度(m)", width = 15)
    @ApiModelProperty(value = "深度(m)")
    private java.lang.Double currentdepth;
	/**最大深度(m)*/
	@Excel(name = "最大深度(m)", width = 15)
    @ApiModelProperty(value = "最大深度(m)")
    private java.lang.Double maxdepth;
	/**流量*/
	@Excel(name = "流量", width = 15)
    @ApiModelProperty(value = "流量")
    private java.lang.Double flow;
	/**前后倾角*/
	@Excel(name = "前后倾角", width = 15)
    @ApiModelProperty(value = "前后倾角")
    private java.lang.Double xangle;
	/**左右倾角*/
	@Excel(name = "左右倾角", width = 15)
    @ApiModelProperty(value = "左右倾角")
    private java.lang.Double yangle;
	/**倾斜度*/
	@Excel(name = "倾斜度", width = 15)
    @ApiModelProperty(value = "倾斜度")
    private java.lang.Double gradient;
	/**电流(A)*/
	@Excel(name = "电流(A)", width = 15)
    @ApiModelProperty(value = "电流(A)")
    private java.lang.Double electricity;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.Double longitudu;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.Double latitude;
	/**关联id*/
	@Excel(name = "关联id", width = 15)
    @ApiModelProperty(value = "关联id")
    private java.lang.String guid;
}
