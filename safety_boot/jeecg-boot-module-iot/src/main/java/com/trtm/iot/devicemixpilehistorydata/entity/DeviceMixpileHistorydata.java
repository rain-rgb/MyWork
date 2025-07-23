package com.trtm.iot.devicemixpilehistorydata.entity;

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
 * @Description: device_mixpile_historydata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_historydata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_historydata对象", description="device_mixpile_historydata")
public class DeviceMixpileHistorydata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**设计深度*/
	@Excel(name = "设计深度", width = 15)
    @ApiModelProperty(value = "设计深度")
    private java.lang.String designdep;
	/**当前深度*/
	@Excel(name = "当前深度", width = 15)
    @ApiModelProperty(value = "当前深度")
    private java.lang.String curdep;
	/**x轴角度*/
	@Excel(name = "x轴角度", width = 15)
    @ApiModelProperty(value = "x轴角度")
    private java.lang.String x;
	/**y轴角度*/
	@Excel(name = "y轴角度", width = 15)
    @ApiModelProperty(value = "y轴角度")
    private java.lang.String y;
	/**当前电流*/
	@Excel(name = "当前电流", width = 15)
    @ApiModelProperty(value = "当前电流")
    private java.lang.String elec;
	/**已灌浆量*/
	@Excel(name = "已灌浆量", width = 15)
    @ApiModelProperty(value = "已灌浆量")
    private java.lang.String grouting;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileno;
	/**设计水灰比*/
	@Excel(name = "设计水灰比", width = 15)
    @ApiModelProperty(value = "设计水灰比")
    private java.lang.String designratio;
	/**设计灌浆量*/
	@Excel(name = "设计灌浆量", width = 15)
    @ApiModelProperty(value = "设计灌浆量")
    private java.lang.String designgrout;
	/**水泥量*/
	@Excel(name = "水泥量", width = 15)
    @ApiModelProperty(value = "水泥量")
    private java.lang.String cement;
	/**用水量*/
	@Excel(name = "用水量", width = 15)
    @ApiModelProperty(value = "用水量")
    private java.lang.String water;
	/**水灰比*/
	@Excel(name = "水灰比", width = 15)
    @ApiModelProperty(value = "水灰比")
    private java.lang.String ratio;
	/**盘次*/
	@Excel(name = "盘次", width = 15)
    @ApiModelProperty(value = "盘次")
    private java.lang.String serial;
	/**操作员*/
	@Excel(name = "操作员", width = 15)
    @ApiModelProperty(value = "操作员")
    private java.lang.String username;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;

	private java.lang.String ltdfloat;

    private java.lang.String lgdfloat;
    private java.lang.String flowlst;
    private java.lang.String speed;
    private java.lang.String worksta;
    private java.lang.String mileage;
    private java.lang.String press;
    private java.lang.String flowmeter;
    private java.lang.Integer piletype;
    private java.lang.String rjrwd;


}
