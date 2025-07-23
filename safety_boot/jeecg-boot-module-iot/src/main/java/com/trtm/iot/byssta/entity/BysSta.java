package com.trtm.iot.byssta.entity;

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
 * @Description: 标养室统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-11
 * @Version: V1.0
 */
@Data
@TableName("bys_sta")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bys_sta对象", description="标养室统计信息表")
public class BysSta implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeino;
	/**统计时间，以天为单位*/
	@Excel(name = "统计时间，以天为单位", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计时间，以天为单位")
    private java.util.Date statisticsTime;
	/**总数*/
	@Excel(name = "总数", width = 15)
    @ApiModelProperty(value = "总数")
    private java.lang.Integer total;
	/**总超标数*/
	@Excel(name = "总超标数", width = 15)
    @ApiModelProperty(value = "总超标数")
    private java.lang.Integer chaobiaonum;
	/**温度超标总数*/
	@Excel(name = "温度超标总数", width = 15)
    @ApiModelProperty(value = "温度超标总数")
    private java.lang.Integer temchaobiao;
	/**湿度超标总数*/
	@Excel(name = "湿度超标总数", width = 15)
    @ApiModelProperty(value = "湿度超标总数")
    private java.lang.Integer humchaobiao;
}
