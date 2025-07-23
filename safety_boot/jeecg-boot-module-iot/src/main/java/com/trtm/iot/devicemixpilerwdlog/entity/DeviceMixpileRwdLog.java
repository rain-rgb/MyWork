package com.trtm.iot.devicemixpilerwdlog.entity;

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
 * @Description: 软基工单下发记录表
 * @Author: jeecg-boot
 * @Date:   2022-03-25
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_rwd_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_rwd_log对象", description="软基工单下发记录表")
public class DeviceMixpileRwdLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeino;
	/**软基工单号*/
	@Excel(name = "软基工单号", width = 15)
    @ApiModelProperty(value = "软基工单号")
    private java.lang.String rjrwd;
	/**下发时间*/
	@Excel(name = "下发时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "下发时间")
    private java.util.Date issuedtime;
	/**工单状态，0：未开始，1：已完成*/
	@Excel(name = "工单状态，0：未开始，1：已完成", width = 15)
    @ApiModelProperty(value = "工单状态，0：未开始，1：已完成")
    private java.lang.Integer status;

    private java.lang.String qbroker;
    private java.lang.String topicAll;
    private java.lang.String pushjson;
}
