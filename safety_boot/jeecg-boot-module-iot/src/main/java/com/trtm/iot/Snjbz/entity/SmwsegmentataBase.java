package com.trtm.iot.Snjbz.entity;

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
 * @Description: 水泥搅拌桩主表
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
@Data
@TableName("smwsegmentata_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smwsegmentata_base对象", description="水泥搅拌桩主表")
public class SmwsegmentataBase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编码*/
	@Excel(name = "设备编码", width = 15)
    @ApiModelProperty(value = "设备编码")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String equipmentcode;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String constructionstakenum;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private Date starttime;
	/**记录状态 */
	@Excel(name = "记录状态 ", width = 15)
    @Dict(dicCode = "recordstatus")
    @ApiModelProperty(value = "记录状态 ")
    private java.lang.Integer recordstatus;
	/**钻杆状态*/
	@Excel(name = "钻杆状态", width = 15)
    @Dict(dicCode = "pipestatus")
    @ApiModelProperty(value = "钻杆状态")
    private java.lang.Integer pipestatus;
	/**喷浆状态*/
	@Excel(name = "喷浆状态", width = 15)
    @Dict(dicCode = "nozzlestatus")
    @ApiModelProperty(value = "喷浆状态")
    private java.lang.Integer nozzlestatus;
	/**水灰比*/
	@Excel(name = "水灰比", width = 15)
    @ApiModelProperty(value = "水灰比")
    private java.lang.Double waterashratio;
	/**段浆量(L)*/
	@Excel(name = "段浆量(L)", width = 15)
    @ApiModelProperty(value = "段浆量(L)")
    private java.lang.Double segmentpulp;
	/**段灰量(kg)*/
	@Excel(name = "段灰量(kg)", width = 15)
    @ApiModelProperty(value = "段灰量(kg)")
    private java.lang.Double segmentash;
	/**累计浆量(L)*/
	@Excel(name = "累计浆量(L)", width = 15)
    @ApiModelProperty(value = "累计浆量(L)")
    private java.lang.Double totalpulp;
	/**累计灰量(kg)*/
	@Excel(name = "累计灰量(kg)", width = 15)
    @ApiModelProperty(value = "累计灰量(kg)")
    private java.lang.Double totalash;
	/**电流(A)*/
	@Excel(name = "电流(A)", width = 15)
    @ApiModelProperty(value = "电流(A)")
    private java.lang.Double electricity;
	/**密度(g/cm3)*/
	@Excel(name = "密度(g/cm3)", width = 15)
    @ApiModelProperty(value = "密度(g/cm3)")
    private java.lang.Double density;
	/**喷压(MPa)*/
	@Excel(name = "喷压(MPa)", width = 15)
    @ApiModelProperty(value = "喷压(MPa)")
    private java.lang.Double pressure;
	/**速度(cm/min)*/
	@Excel(name = "速度(cm/min)", width = 15)
    @ApiModelProperty(value = "速度(cm/min)")
    private java.lang.Double speed;
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
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.Double longitudu;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.Double latitude;
	/**与子表关联*/
	@Excel(name = "与子表关联", width = 15)
    @ApiModelProperty(value = "与子表关联")
    private java.lang.String guid;
	/**状态在线不在线*/
	@Excel(name = "状态在线不在线", width = 15)
    @ApiModelProperty(value = "状态在线不在线")
    private java.lang.Integer status;
}
