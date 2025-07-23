package com.trtm.iot.mixpileshebeicfg.entity;

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
 * @Description: 搅拌设备参数配置表
 * @Author: jeecg-boot
 * @Date:   2024-01-24
 * @Version: V1.0
 */
@Data
@TableName("mixpile_shebeicfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="mixpile_shebeicfg对象", description="搅拌设备参数配置表")
public class MixpileShebeicfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**拌叶片的总枚数*/
	@Excel(name = "拌叶片的总枚数", width = 15)
    @ApiModelProperty(value = "拌叶片的总枚数")
    private java.lang.Integer mixpileZ;
	/**搅拌叶片的宽度(m)*/
	@Excel(name = "搅拌叶片的宽度(m)", width = 15)
    @ApiModelProperty(value = "搅拌叶片的宽度(m)")
    private java.lang.Double mixpileH;
	/**搅拌头的回转数(r/min)*/
	@Excel(name = "搅拌头的回转数(r/min)", width = 15)
    @ApiModelProperty(value = "搅拌头的回转数(r/min)")
    private java.lang.Double mixpileN;
	/**搅拌叶片与搅拌轴的垂直夹角*/
	@Excel(name = "搅拌叶片与搅拌轴的垂直夹角", width = 15)
    @ApiModelProperty(value = "搅拌叶片与搅拌轴的垂直夹角")
    private java.lang.Double mixpileB;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
