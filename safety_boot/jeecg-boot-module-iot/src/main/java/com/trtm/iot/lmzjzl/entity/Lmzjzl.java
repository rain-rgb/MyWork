package com.trtm.iot.lmzjzl.entity;

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
 * @Description: 路面质检资料
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Data
@TableName("lmzjzl")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="lmzjzl对象", description="路面质检资料")
public class Lmzjzl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**配方号*/
	@Excel(name = "配方号", width = 15)
    @ApiModelProperty(value = "配方号")
    private java.lang.String formulaName;
	/**卸料前温度*/
	@Excel(name = "卸料前温度", width = 15)
    @ApiModelProperty(value = "卸料前温度")
    private java.lang.String beforetemperature;
	/**摊铺温度*/
	@Excel(name = "摊铺温度", width = 15)
    @ApiModelProperty(value = "摊铺温度")
    private java.lang.String pavingtemperature;
	/**初压温度*/
	@Excel(name = "初压温度", width = 15)
    @ApiModelProperty(value = "初压温度")
    private java.lang.String temperatureFirst;
	/**复压温度*/
	@Excel(name = "复压温度", width = 15)
    @ApiModelProperty(value = "复压温度")
    private java.lang.String temperatureRepeat;
	/**终压温度*/
	@Excel(name = "终压温度", width = 15)
    @ApiModelProperty(value = "终压温度")
    private java.lang.String finallytemperature;
	/**初压遍数*/
	@Excel(name = "初压遍数", width = 15)
    @ApiModelProperty(value = "初压遍数")
    private java.lang.String timesFirst;
	/**复压变数*/
	@Excel(name = "复压变数", width = 15)
    @ApiModelProperty(value = "复压变数")
    private java.lang.String timesRepeat;
	/**小于7占比*/
	@Excel(name = "小于7占比", width = 15)
    @ApiModelProperty(value = "小于7占比")
    private java.lang.String Lessthan7;
	/**等于7占比*/
	@Excel(name = "等于7占比", width = 15)
    @ApiModelProperty(value = "等于7占比")
    private java.lang.String Equalto7;
	/**等于8占比*/
	@Excel(name = "等于8占比", width = 15)
    @ApiModelProperty(value = "等于8占比")
    private java.lang.String Equalto8;
	/**大于8占比*/
	@Excel(name = "大于8占比", width = 15)
    @ApiModelProperty(value = "大于8占比")
    private java.lang.String Greaterthan8;
	/**时间*/
	@Excel(name = "时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private java.util.Date date;
}
