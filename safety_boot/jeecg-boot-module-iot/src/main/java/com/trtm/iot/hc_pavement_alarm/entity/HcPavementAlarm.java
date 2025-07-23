package com.trtm.iot.hc_pavement_alarm.entity;

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
 * @Description: 摊铺碾压预警
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Data
@TableName("hc_pavement_alarm")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_pavement_alarm对象", description="摊铺碾压预警")
public class HcPavementAlarm implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远*/
	@Excel(name = "告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远", width = 15)
    @ApiModelProperty(value = "告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远")
    @Dict(dicCode = "alarmtypeid")
    private java.lang.Integer alarmtypeid;
	/**机械类型id*/
	@Excel(name = "机械类型id", width = 15)
    @ApiModelProperty(value = "机械类型id")
    private java.lang.String machinetypeid;
	/**告警级别，0为无级别，1为轻微，2为一般，3为严重*/
	@Excel(name = "告警级别，0为无级别，1为轻微，2为一般，3为严重", width = 15)
    @ApiModelProperty(value = "告警级别，0为无级别，1为轻微，2为一般，3为严重")
    @Dict(dicCode = "alarmlevel")
    private java.lang.Integer alarmlevel;
	/**设备sn号*/
	@Excel(name = "设备sn号", width = 15)
    @ApiModelProperty(value = "设备sn号")
    private java.lang.String sncode;
	/**机械id*/
	@Excel(name = "机械id", width = 15)
    @ApiModelProperty(value = "机械id")
    private java.lang.String machineid;
	/**机械名称*/
	@Excel(name = "机械名称", width = 15)
    @ApiModelProperty(value = "机械名称")
    private java.lang.String machinename;
	/**告警时间*/
	@Excel(name = "告警时间", width = 15, format = "yyyy-MM-dd hh:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "告警时间")
    private java.util.Date datatime;
	/**标准值*/
	@Excel(name = "标准值", width = 15)
    @ApiModelProperty(value = "标准值")
    private java.lang.String standard;
	/**真实值*/
	@Excel(name = "真实值", width = 15)
    @ApiModelProperty(value = "真实值")
    private java.lang.String actual;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String lon;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String lat;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String roadstation;
	/**左右幅，左负右正*/
	@Excel(name = "左右幅，左负右正", width = 15)
    @ApiModelProperty(value = "左右幅，左负右正")
    private java.lang.String roadoffset;
	/**告警内容*/
	@Excel(name = " 告警内容", width = 15)
    @ApiModelProperty(value = " 告警内容")
    private java.lang.String alarminfo;
	/**projectid*/
	@Excel(name = "projectid", width = 15)
    @ApiModelProperty(value = "projectid")
    private java.lang.String projectid;
	/**sectionid*/
	@Excel(name = "sectionid", width = 15)
    @ApiModelProperty(value = "sectionid")
    private java.lang.String sectionid;

    /**工程名*/
    @Excel(name = "工程名", width = 15)
    @ApiModelProperty(value = "工程名")
    private java.lang.String pjname;
    /**标段名*/
    @Excel(name = "标段名", width = 15)
    @ApiModelProperty(value = "标段名")
    private java.lang.String sectionname;
    private Integer overproofStatus;
    private Integer type;
}
