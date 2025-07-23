package com.trtm.iot.substation_bs.entity;

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
 * @Description: 变电站数据（历史数据）
 * @Author: jeecg-boot
 * @Date:   2025-05-13
 * @Version: V1.0
 */
@Data
@TableName("substation_bs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="substation_bs对象", description="变电站数据（历史数据）")
public class SubstationBs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**deviceSn*/
	@Excel(name = "deviceSn", width = 15)
    @ApiModelProperty(value = "deviceSn")
    private java.lang.String deviceSn;
	/**stustus*/
	@Excel(name = "stustus", width = 15)
    @ApiModelProperty(value = "stustus")
    private java.lang.String stustus;
	/**creatime*/
	@Excel(name = "creatime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "creatime")
    private java.util.Date creatime;
	/**info*/
	@Excel(name = "info", width = 15)
    @ApiModelProperty(value = "info")
    private java.lang.String info;
	/**alarmStatus*/
	@Excel(name = "alarmStatus", width = 15)
    @ApiModelProperty(value = "alarmStatus")
    private java.lang.String alarmStatus;
}
