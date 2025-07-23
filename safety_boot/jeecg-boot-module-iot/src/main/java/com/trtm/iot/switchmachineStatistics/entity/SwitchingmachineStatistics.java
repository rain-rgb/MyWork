package com.trtm.iot.switchmachineStatistics.entity;

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
 * @Description: 拌合站设备开关机统计表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
@Data
@TableName("switchingmachine_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="switchingmachine_statistics对象", description="拌合站设备开关机统计表")
public class SwitchingmachineStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**最新数据时间*/
	@Excel(name = "最新数据时间", width = 15)
    @ApiModelProperty(value = "最新数据时间")
    private java.lang.String chuliaoshijian;
	/**1 已经开机  2已经关机*/
	@Excel(name = "1 已经开机  2已经关机", width = 15)
    @ApiModelProperty(value = "1 已经开机  2已经关机")
    private java.lang.Integer status;
	/**关机时间*/
	@Excel(name = "关机时间", width = 15)
    @ApiModelProperty(value = "关机时间")
    private java.lang.String jieshushijian;
	/**开机时间*/
	@Excel(name = "开机时间", width = 15)
    @ApiModelProperty(value = "开机时间")
    private java.lang.String kaijishijian;
}
