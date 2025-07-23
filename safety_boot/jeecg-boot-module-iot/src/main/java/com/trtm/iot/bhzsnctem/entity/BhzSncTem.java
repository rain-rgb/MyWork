package com.trtm.iot.bhzsnctem.entity;

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
 * @Description: bhz_snc_tem
 * @Author: jeecg-boot
 * @Date:   2023-11-09
 * @Version: V1.0
 */
@Data
@TableName("bhz_snc_tem")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_snc_tem对象", description="bhz_snc_tem")
public class BhzSncTem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**节点*/
	@Excel(name = "节点", width = 15)
    @ApiModelProperty(value = "节点")
    private java.lang.Integer nodeid;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.String tem;
	/**湿度*/
	@Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private java.lang.String hum;
	/**记录时间*/
	@Excel(name = "记录时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "记录时间")
    private java.lang.String recordtime;
	/**coordinatetype*/
	@Excel(name = "coordinatetype", width = 15)
    @ApiModelProperty(value = "coordinatetype")
    private java.lang.String coordinatetype;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String lng;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String lat;
	/**temh*/
	@Excel(name = "temh", width = 15)
    @ApiModelProperty(value = "temh")
    private java.lang.String temh;
	/**teml*/
	@Excel(name = "teml", width = 15)
    @ApiModelProperty(value = "teml")
    private java.lang.String teml;
	/**humh*/
	@Excel(name = "humh", width = 15)
    @ApiModelProperty(value = "humh")
    private java.lang.String humh;
	/**huml*/
	@Excel(name = "huml", width = 15)
    @ApiModelProperty(value = "huml")
    private java.lang.String huml;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String deviceno;
	/**remar*/
	@Excel(name = "remar", width = 15)
    @ApiModelProperty(value = "remar")
    private java.lang.String remar;
}
