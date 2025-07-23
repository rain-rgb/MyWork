package com.trtm.iot.weiyandevice.entity;

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
 * @Description: 隧道环境（有害气体）监测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Data
@TableName("weiyan_device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="weiyan_device对象", description="隧道环境（有害气体）监测主表")
public class WeiyanDevice implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**氧气*/
	@Excel(name = "氧气", width = 15)
    @ApiModelProperty(value = "氧气")
    private java.lang.String oxygen;
    /**甲烷*/
    @Excel(name = "甲烷", width = 15)
    @ApiModelProperty(value = "甲烷")
    private java.lang.String methane;
    /**硫化氢*/
    @Excel(name = "硫化氢", width = 15)
    @ApiModelProperty(value = "硫化氢")
    private java.lang.String hSulfide;
    /**一氧化碳*/
    @Excel(name = "一氧化碳", width = 15)
    @ApiModelProperty(value = "一氧化碳")
    private java.lang.String cMonoxide;
    /**二氧化碳*/
    @Excel(name = "二氧化碳", width = 15)
    @ApiModelProperty(value = "二氧化碳")
    private java.lang.String cDioxide;
    /**地点*/
    @Excel(name = "地点", width = 15)
    @ApiModelProperty(value = "地点")
    private java.lang.String site;
	/**报警类型 0无报警，1低报警，2高报警*/
	@Excel(name = "报警类型 0无报警，1低报警，2高报警", width = 15)
    @ApiModelProperty(value = "报警类型 0无报警，1低报警，2高报警")
    private java.lang.Integer alarmtype;
	/**上报时间*/
	@Excel(name = "上报时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上报时间")
    private java.util.Date uploadtime;
}
