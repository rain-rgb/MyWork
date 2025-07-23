package com.trtm.iot.Snjbz.entity;

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
 * @Description: 水泥搅拌桩子表
 * @Author: jeecg-boot
 * @Date:   2021-04-16
 * @Version: V1.0
 */
@Data
@TableName("smwsegmentdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smwsegmentdata对象", description="水泥搅拌桩子表")
public class Smwsegmentdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**与主表关联*/
	@Excel(name = "与主表关联", width = 15)
    @ApiModelProperty(value = "与主表关联")
    private java.lang.String guid;
	/**段深度(m)*/
	@Excel(name = "段深度(m)", width = 15)
    @ApiModelProperty(value = "段深度(m)")
    private java.lang.Double segmentdepth;
	/**段浆量(L)*/
	@Excel(name = "段浆量(L)", width = 15)
    @ApiModelProperty(value = "段浆量(L)")
    private java.lang.Double segmentpulp;
	/**段灰量(kg)*/
	@Excel(name = "段灰量(kg)", width = 15)
    @ApiModelProperty(value = "段灰量(kg)")
    private java.lang.Double segmentash;
	/**段电流(A)*/
	@Excel(name = "段电流(A)", width = 15)
    @ApiModelProperty(value = "段电流(A)")
    private java.lang.Double segmentelectricity;
	/**段密度(g/cm3)*/
	@Excel(name = "段密度(g/cm3)", width = 15)
    @ApiModelProperty(value = "段密度(g/cm3)")
    private java.lang.Double segmentdensity;
	/**段喷压(MPa)*/
	@Excel(name = "段喷压(MPa)", width = 15)
    @ApiModelProperty(value = "段喷压(MPa)")
    private java.lang.Double segmentpressure;
	/**速度(cm/min)*/
	@Excel(name = "速度(cm/min)", width = 15)
    @ApiModelProperty(value = "速度(cm/min)")
    private java.lang.Double segmentspeed;
	/**段时长(s)*/
	@Excel(name = "段时长(s)", width = 15)
    @ApiModelProperty(value = "段时长(s)")
    private java.lang.String segmentage;
}
