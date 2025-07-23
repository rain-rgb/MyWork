package com.trtm.iot.carmiles.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 车辆里程
 * @Author: jeecg-boot
 * @Date:   2022-03-08
 * @Version: V1.0
 */
@Data
@TableName("car_miles")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="car_miles对象", description="车辆里程")
public class Carmiles {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
	private java.lang.String shebeiNo;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
	private java.lang.Float miles;
	/**运行时长(秒)*/
	@Excel(name = "运行时长(秒)", width = 15)
    @ApiModelProperty(value = "运行时长(秒)")
	private java.lang.Integer runtime;
	/**运行日期*/
	@Excel(name = "运行日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "运行日期")
	private java.util.Date rundate;
	/**任务执行时间*/
	@Excel(name = "任务执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "任务执行时间")
	private java.util.Date jobruntime;
}
