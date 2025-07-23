package com.trtm.iot.devicehistorystatic.entity;

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
 * @Description: 环境监测数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-07-09
 * @Version: V1.0
 */
@Data
@TableName("devicehistory_static")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="devicehistory_static对象", description="环境监测数据统计表")
public class DevicehistoryStatic implements Serializable {
    private static final long serialVersionUID = 1L;

	/**唯一标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.Integer id;
	/**设备地址*/
	@Excel(name = "设备地址", width = 15)
    @ApiModelProperty(value = "设备地址")
    private java.lang.String devaddr;
	/**总数*/
	@Excel(name = "总数", width = 15)
    @ApiModelProperty(value = "总数")
    private java.lang.Integer total;
	/**超标总数*/
	@Excel(name = "超标总数", width = 15)
    @ApiModelProperty(value = "超标总数")
    private java.lang.Integer chaototal;
	/**初级超标数*/
	@Excel(name = "初级超标数", width = 15)
    @ApiModelProperty(value = "初级超标数")
    private java.lang.Integer primaryTotal;
	/**pm25超标总数*/
	@Excel(name = "pm25超标总数", width = 15)
    @ApiModelProperty(value = "pm25超标总数")
    private java.lang.Integer pm25chao;
	/**pm10超标总数*/
	@Excel(name = "pm10超标总数", width = 15)
    @ApiModelProperty(value = "pm10超标总数")
    private java.lang.Integer pm10chao;
	/**噪音超标总数*/
	@Excel(name = "噪音超标总数", width = 15)
    @ApiModelProperty(value = "噪音超标总数")
    private java.lang.Integer noisechao;
	/**温度超标数*/
	@Excel(name = "温度超标数", width = 15)
    @ApiModelProperty(value = "温度超标数")
    private java.lang.Integer temchao;
	/**湿度超标数*/
	@Excel(name = "湿度超标数", width = 15)
    @ApiModelProperty(value = "湿度超标数")
    private java.lang.Integer humchao;
	/**风力超标数*/
	@Excel(name = "风力超标数", width = 15)
    @ApiModelProperty(value = "风力超标数")
    private java.lang.Integer winchao;
	/**风速超标数*/
	@Excel(name = "风速超标数", width = 15)
    @ApiModelProperty(value = "风速超标数")
    private java.lang.Integer winsdchao;
	/**统计时间，以天为单位*/
	@Excel(name = "统计时间，以天为单位", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计时间，以天为单位")
    private java.util.Date statisticsTime;
	/**中级超标数*/
	@Excel(name = "中级超标数", width = 15)
    @ApiModelProperty(value = "中级超标数")
    private java.lang.Integer middleTotal;
	/**高级超标数*/
	@Excel(name = "高级超标数", width = 15)
    @ApiModelProperty(value = "高级超标数")
    private java.lang.Integer advanceTotal;
	/**pm25初级超标数*/
	@Excel(name = "pm25初级超标数", width = 15)
    @ApiModelProperty(value = "pm25初级超标数")
    private java.lang.Integer pm25primary;
	/**pm25中级超标数*/
	@Excel(name = "pm25中级超标数", width = 15)
    @ApiModelProperty(value = "pm25中级超标数")
    private java.lang.Integer pm25middle;
	/**pm25高级超标数*/
	@Excel(name = "pm25高级超标数", width = 15)
    @ApiModelProperty(value = "pm25高级超标数")
    private java.lang.Integer pm25advance;
	/**pm10初级超标数*/
	@Excel(name = "pm10初级超标数", width = 15)
    @ApiModelProperty(value = "pm10初级超标数")
    private java.lang.Integer pm10primary;
	/**pm10中级超标数*/
	@Excel(name = "pm10中级超标数", width = 15)
    @ApiModelProperty(value = "pm10中级超标数")
    private java.lang.Integer pm10middle;
	/**pm10高级超标数*/
	@Excel(name = "pm10高级超标数", width = 15)
    @ApiModelProperty(value = "pm10高级超标数")
    private java.lang.Integer pm10advance;
	/**噪声初级超标数*/
	@Excel(name = "噪声初级超标数", width = 15)
    @ApiModelProperty(value = "噪声初级超标数")
    private java.lang.Integer noiseprimary;
	/**噪声中级超标数*/
	@Excel(name = "噪声中级超标数", width = 15)
    @ApiModelProperty(value = "噪声中级超标数")
    private java.lang.Integer noisemiddle;
	/**噪声高级超标数*/
	@Excel(name = "噪声高级超标数", width = 15)
    @ApiModelProperty(value = "噪声高级超标数")
    private java.lang.Integer noiseadvance;
}
