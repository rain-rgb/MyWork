package com.trtm.iot.devicehammeringhistorydata.entity;

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
 * @Description: device_hammering_historydata
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
@Data
@TableName("device_hammering_historydata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_hammering_historydata对象", description="device_hammering_historydata")
public class DeviceHammeringHistorydata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileno;
	/**设计桩长(m)*/
	@Excel(name = "设计桩长(m)", width = 15)
    @ApiModelProperty(value = "设计桩长(m)")
    private java.lang.String designdep;
	/**当前桩长度(m)*/
	@Excel(name = "当前桩长度(m)", width = 15)
    @ApiModelProperty(value = "当前桩长度(m)")
    private java.lang.String curdep;
	/**离地桩长(m)*/
	@Excel(name = "离地桩长(m)", width = 15)
    @ApiModelProperty(value = "离地桩长(m)")
    private java.lang.String grounddep;
	/**最大垂直度(%)*/
	@Excel(name = "最大垂直度(%)", width = 15)
    @ApiModelProperty(value = "最大垂直度(%)")
    private java.lang.String pileY;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private java.lang.String pileStarttime;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
	/**速度*/
	@Excel(name = "速度", width = 15)
    @ApiModelProperty(value = "速度")
    private java.lang.String speed;
	/**接桩时间*/
	@Excel(name = "接桩时间", width = 15)
    @ApiModelProperty(value = "接桩时间")
    private java.lang.String piletime;
	/**有效桩长(m)*/
	@Excel(name = "有效桩长(m)", width = 15)
    @ApiModelProperty(value = "有效桩长(m)")
    private java.lang.String pileEffdep;
	/**时长*/
	@Excel(name = "时长", width = 15)
    @ApiModelProperty(value = "时长")
    private java.lang.String pileWorktime;
	/**打桩次数*/
	@Excel(name = "打桩次数", width = 15)
    @ApiModelProperty(value = "打桩次数")
    private java.lang.Integer times;
	/**工作状态*/
	@Excel(name = "工作状态", width = 15)
    @ApiModelProperty(value = "工作状态")
    private java.lang.String worksta;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.String ts;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String ltdfloat;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String lgdfloat;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String mileage;
	/**锤击次数*/
	@Excel(name = "锤击次数", width = 15)
    @ApiModelProperty(value = "锤击次数")
    private java.lang.Integer pileSecond;
}
