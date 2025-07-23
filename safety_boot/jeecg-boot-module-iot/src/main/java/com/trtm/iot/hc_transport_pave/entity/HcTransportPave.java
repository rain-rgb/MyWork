package com.trtm.iot.hc_transport_pave.entity;

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
 * @Description: 混合料运输子表
 * @Author: jeecg-boot
 * @Date:   2023-04-28
 * @Version: V1.0
 */
@Data
@TableName("hc_transport_pave")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_transport_pave对象", description="混合料运输子表")
public class HcTransportPave implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**机械id*/
	@Excel(name = "机械id", width = 15)
    @ApiModelProperty(value = "机械id")
    private java.lang.Integer machineid;
	/**机械名称*/
	@Excel(name = "机械名称", width = 15)
    @ApiModelProperty(value = "机械名称")
    private java.lang.String machinename;
	/**开始摊铺时间*/
	@Excel(name = "开始摊铺时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始摊铺时间")
    private java.util.Date startpavetime;
	/**结束摊铺时间*/
	@Excel(name = "结束摊铺时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束摊铺时间")
    private java.util.Date endpavetime;
	/**摊铺时长*/
	@Excel(name = " 摊铺时长", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = " 摊铺时长")
    private java.util.Date pavetime;
	/**开始摊铺桩号*/
	@Excel(name = " 开始摊铺桩号", width = 15)
    @ApiModelProperty(value = " 开始摊铺桩号")
    private java.lang.String startpavestation;
	/**结束摊铺桩号*/
	@Excel(name = " 结束摊铺桩号", width = 15)
    @ApiModelProperty(value = " 结束摊铺桩号")
    private java.lang.String endpavestation;
}
