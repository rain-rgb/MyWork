package com.trtm.iot.devicepiplewall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: device_piplewall_one
 * @Author: jeecg-boot
 * @Date:   2024-12-28
 * @Version: V1.0
 */
@Data
@TableName("device_piplewall_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_piplewall_one对象", description="device_piplewall_one")
public class DevicePiplewallOnetj implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")

    private java.lang.String shebeino;
    @ApiModelProperty(value = "接桩次数")
    private java.lang.Integer jiegantimes;

	/**结束时间*/
	@Excel(name = "时间", width = 15)
    @ApiModelProperty(value = "时间")
    private String pileEndtime;

	/**下钻速度(cm/min)*/
	@Excel(name = "总桩数", width = 15)
    @ApiModelProperty(value = "下钻速度(cm/min)")
    private String downSpeed;
	/**提钻速度(cm/min)*/
	@Excel(name = "不合格桩数", width = 15)
    @ApiModelProperty(value = "提钻速度(cm/min)")
    private Double upSpeed;
    /**施工深度(m)*/
    @Excel(name = "施工深度(m)", width = 15)
    @ApiModelProperty(value = "施工深度(m)")
    private String pileRealdep;
	/**总方量m³*/
	@Excel(name = "总方量m³", width = 15)
    @ApiModelProperty(value = "总方量m³")
    private String zongm;

}
