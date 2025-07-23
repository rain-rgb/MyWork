package com.trtm.iot.hc_transportrecords.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 运输数据子表
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
@ApiModel(value="hc_transportrecords_pave对象", description="运输数据子表")
@Data
@TableName("hc_transportrecords_pave")
public class HcTransportrecordsPave implements Serializable {
    private static final long serialVersionUID = 1L;

	/**记录id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "记录id")
    private java.lang.Integer id;
	/**机械id*/
	@Excel(name = "机械id", width = 15)
    @ApiModelProperty(value = "机械id")
    private java.lang.String machineid;
	/**机械id*/
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
	@Excel(name = "摊铺时长", width = 15)
    @ApiModelProperty(value = "摊铺时长")
    private java.lang.String pavetime;
	/**开始摊铺桩号*/
	@Excel(name = "开始摊铺桩号", width = 15)
    @ApiModelProperty(value = "开始摊铺桩号")
    private java.lang.String startpavestation;
	/**结束摊铺桩号*/
	@Excel(name = "结束摊铺桩号", width = 15)
    @ApiModelProperty(value = "结束摊铺桩号")
    private java.lang.String endpavestation;
	/**主表id*/
    @ApiModelProperty(value = "主表id")
    private java.lang.Integer zbid;
}
