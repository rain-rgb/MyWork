package com.trtm.iot.bys.entity;

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
 * @Description: bys_real
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Data
@TableName("bys_real")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bys_real对象", description="bys_real")
public class BysReal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**上传时间*/
	@Excel(name = "上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上传时间")
    private Date gatherdate;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private String shebeino;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private BigDecimal temperature;
	/**湿度*/
	@Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private BigDecimal humidity;
	/**唯一标识*/
	@Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private String guid;
	/**数据转发状态0未转发1已转发2转发错误3未配置appkey*/
	@Excel(name = "数据转发状态0未转发1已转发2转发错误3未配置appkey", width = 15)
    @ApiModelProperty(value = "数据转发状态0未转发1已转发2转发错误3未配置appkey")
    private Integer forwardstatus;
    /**温度超标状态*/
    @Excel(name = "温度超标状态", width = 15)
    @ApiModelProperty(value = "温度超标状态")
    private java.lang.Integer temstatus;
    /**湿度超标状态*/
    @Excel(name = "湿度超标状态", width = 15)
    @ApiModelProperty(value = "湿度超标状态")
    private java.lang.Integer humstatus;
    @Excel(name = "是否超标", width = 10)
    @ApiModelProperty(value = "是否超标")
    private java.lang.Integer alertstate;
    @Excel(name = "状态", width = 10)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**数据上传时间*/
    @Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private Date datatime;

    private Integer istuisong;
    private String sysOrgCode;
}
