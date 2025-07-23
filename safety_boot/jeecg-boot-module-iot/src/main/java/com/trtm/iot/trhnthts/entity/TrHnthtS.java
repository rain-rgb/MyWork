package com.trtm.iot.trhnthts.entity;

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
 * @Description: 混凝土回弹子表
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
@Data
@TableName("tr_hntht_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_hntht_s对象", description="混凝土回弹子表")
public class TrHnthtS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**关联主表*/
	@Excel(name = "关联主表", width = 15)
    @ApiModelProperty(value = "关联主表")
    private java.lang.String testid;
	/**测区回弹平均值*/
	@Excel(name = "测区回弹平均值", width = 15)
    @ApiModelProperty(value = "测区回弹平均值")
    private java.lang.Double average;
	/**测区强度值*/
	@Excel(name = "测区强度值", width = 15)
    @ApiModelProperty(value = "测区强度值")
    private java.lang.Double carbonize;
	/**测区强度值*/
	@Excel(name = "测区强度值", width = 15)
    @ApiModelProperty(value = "测区强度值")
    private java.lang.String strcar;
	/**面修正值*/
	@Excel(name = "面修正值", width = 15)
    @ApiModelProperty(value = "面修正值")
    private java.lang.String calsurface;
	/**角度修正值*/
	@Excel(name = "角度修正值", width = 15)
    @ApiModelProperty(value = "角度修正值")
    private java.lang.String calangle;
	/**0：浇筑侧面；1：浇筑底面*/
	@Excel(name = "0：浇筑侧面；1：浇筑底面", width = 15)
    @ApiModelProperty(value = "0：浇筑侧面；1：浇筑底面")
    private java.lang.Integer pouringsurface;
	/**1：表面, 2：底面, 3：侧面*/
	@Excel(name = "1：表面, 2：底面, 3：侧面", width = 15)
    @ApiModelProperty(value = "1：表面, 2：底面, 3：侧面")
    private java.lang.Integer surface;
	/**碳化值*/
	@Excel(name = "碳化值", width = 15)
    @ApiModelProperty(value = "碳化值")
    private java.lang.Double carbonization;
    /**碳化值2*/
    @Excel(name = "碳化值2", width = 15)
    @ApiModelProperty(value = "碳化值2")
    private java.lang.Double carbonizetwo;
    /**碳化值3*/
    @Excel(name = "碳化值3", width = 15)
    @ApiModelProperty(value = "碳化值3")
    private java.lang.Double carbonizethree;
	/**0：否；1：是*/
	@Excel(name = "0：否；1：是", width = 15)
    @ApiModelProperty(value = "0：否；1：是")
    private java.lang.Integer ispumping;
	/**检测标准依据*/
	@Excel(name = "检测标准依据", width = 15)
    @ApiModelProperty(value = "检测标准依据")
    private java.lang.String standardid;
	/**flagcarbonization*/
	@Excel(name = "flagcarbonization", width = 15)
    @ApiModelProperty(value = "flagcarbonization")
    private java.lang.Double flagcarbonization;
    /**混凝土回弹值*/
    @Excel(name = "混凝土回弹值", width = 15)
    @ApiModelProperty(value = "混凝土回弹值")
    private java.lang.String number;
}
