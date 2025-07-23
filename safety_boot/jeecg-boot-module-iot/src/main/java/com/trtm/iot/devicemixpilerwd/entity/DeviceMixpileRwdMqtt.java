package com.trtm.iot.devicemixpilerwd.entity;

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
 * @Description: device_mixpile_rwd_mqtt
 * @Author: jeecg-boot
 * @Date:   2023-12-28
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_rwd_mqtt")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_rwd_mqtt对象", description="device_mixpile_rwd_mqtt")
public class DeviceMixpileRwdMqtt implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**代理地址*/
	@Excel(name = "代理地址", width = 15)
    @ApiModelProperty(value = "代理地址")
    private java.lang.String qbroker;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String orgcode;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**订阅地址*/
	@Excel(name = "订阅地址", width = 15)
    @ApiModelProperty(value = "订阅地址")
    private java.lang.String topic1;
	/**客户端id*/
	@Excel(name = "客户端id", width = 15)
    @ApiModelProperty(value = "客户端id")
    private java.lang.String clientid;
	/**topicAll*/
	@Excel(name = "topicAll", width = 15)
    @ApiModelProperty(value = "topicAll")
    private java.lang.String topicAll;
    /**限制下发次数*/
    private java.lang.Integer usecount;
}
