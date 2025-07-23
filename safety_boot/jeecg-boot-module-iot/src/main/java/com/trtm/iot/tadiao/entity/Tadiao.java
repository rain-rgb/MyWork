package com.trtm.iot.tadiao.entity;

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
 * @Description: 塔吊
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Data
@TableName("tadiao")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tadiao对象", description="塔吊")
public class Tadiao implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编码*/
	@Excel(name = "设备编码", width = 15)
    @ApiModelProperty(value = "设备编码")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String devicesn;
	/**回转*/
	@Excel(name = "回转", width = 15)
    @ApiModelProperty(value = "回转")
    private java.lang.String angle;
	/**吊绳倍率*/
	@Excel(name = "吊绳倍率", width = 15)
    @ApiModelProperty(value = "吊绳倍率")
    private java.lang.String fall;
	/**吊钩高度*/
	@Excel(name = "吊钩高度", width = 15)
    @ApiModelProperty(value = "吊钩高度")
    private java.lang.String height;
	/**吊重*/
	@Excel(name = "吊重", width = 15)
    @ApiModelProperty(value = "吊重")
    private java.lang.String heavy;
	/**力矩百分比*/
	@Excel(name = "力矩百分比", width = 15)
    @ApiModelProperty(value = "力矩百分比")
    private java.lang.String percent;
	/**倾角角度*/
	@Excel(name = "倾角角度", width = 15)
    @ApiModelProperty(value = "倾角角度")
    private java.lang.String obliqueangle;
	/**倾角方向*/
	@Excel(name = "倾角方向", width = 15)
    @ApiModelProperty(value = "倾角方向")
    private java.lang.String obliquedirection;
	/**风速*/
	@Excel(name = "风速", width = 15)
    @ApiModelProperty(value = "风速")
    private java.lang.String windspeed;
	/**幅度*/
	@Excel(name = "幅度", width = 15)
    @ApiModelProperty(value = "幅度")
    private java.lang.String radius;
	/**设备数据时间*/
    @Excel(name = "设备数据时间", width = 15)
    @ApiModelProperty(value = "设备数据时间")
    private java.lang.String devicetime;
	/**司机身份证编号*/
	@Excel(name = "司机身份证编号", width = 15)
    @ApiModelProperty(value = "司机身份证编号")
    private java.lang.String driverid;
}
