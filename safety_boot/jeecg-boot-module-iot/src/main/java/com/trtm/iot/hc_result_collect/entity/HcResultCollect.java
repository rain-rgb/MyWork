package com.trtm.iot.hc_result_collect.entity;

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
 * @Description: 摊铺里程分布
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("hc_result_collect")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_result_collect对象", description="摊铺里程分布")
public class HcResultCollect implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@Excel(name = "主键id", width = 15)
    @ApiModelProperty(value = "主键id")
    private java.lang.String blockid;
	/**日期*/
	@Excel(name = "日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private java.util.Date paveday;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date begintime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endtime;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectname;
	/**工程id*/
	@Excel(name = "工程id", width = 15)
    @ApiModelProperty(value = "工程id")
    private java.lang.String projectid;
	/**标段名称*/
	@Excel(name = "标段名称", width = 15)
    @ApiModelProperty(value = "标段名称")
    private java.lang.String sectionname;
	/**标段id*/
	@Excel(name = "标段id", width = 15)
    @ApiModelProperty(value = "标段id")
    private java.lang.String sectionid;
	/**分层名称*/
	@Excel(name = "分层名称", width = 15)
    @ApiModelProperty(value = "分层名称")
    private java.lang.String layername;
	/**分层id*/
	@Excel(name = "分层id", width = 15)
    @ApiModelProperty(value = "分层id")
    private java.lang.String layerindex;
	/**开始桩号*/
	@Excel(name = " 开始桩号", width = 15)
    @ApiModelProperty(value = " 开始桩号")
    private java.lang.String beginstation;
	/**格式化后的开始桩号*/
	@Excel(name = "格式化后的开始桩号", width = 15)
    @ApiModelProperty(value = "格式化后的开始桩号")
    private java.lang.String beginstationformat;
	/**结束桩号*/
	@Excel(name = "结束桩号", width = 15)
    @ApiModelProperty(value = "结束桩号")
    private java.lang.String endstation;
	/**格式化后的结束桩号*/
	@Excel(name = "格式化后的结束桩号", width = 15)
    @ApiModelProperty(value = "格式化后的结束桩号")
    private java.lang.String endstationformat;
	/**幅,0-左幅,1-右幅,2-全幅*/
	@Excel(name = "幅,0-左幅,1-右幅,2-全幅", width = 15)
    @ApiModelProperty(value = "幅,0-左幅,1-右幅,2-全幅")
    private java.lang.Integer roadside;
	/**分层方式,0-自动,1-手动*/
	@Excel(name = "分层方式,0-自动,1-手动", width = 15)
    @ApiModelProperty(value = "分层方式,0-自动,1-手动")
    private java.lang.Integer createtype;
	/**摊铺厚度*/
	@Excel(name = "摊铺厚度", width = 15)
    @ApiModelProperty(value = "摊铺厚度")
    private java.lang.String thickavg;
	/**摊铺里程*/
	@Excel(name = "摊铺里程", width = 15)
    @ApiModelProperty(value = "摊铺里程")
    private java.lang.String pavemileage;
}
