package com.trtm.iot.tsyjzbStatistics.entity;

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
 * @Description: t_syjzb_statistics
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
@Data
@TableName("t_syjzb_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="t_syjzb_statistics对象", description="t_syjzb_statistics")
public class TSyjzbStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;

	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String sbbh;
	/**试验类型*/
	@Excel(name = "试验类型", width = 15)
    @ApiModelProperty(value = "试验类型")
    private java.lang.String sylx;
	/**委托编号*/
	@Excel(name = "委托编号", width = 15)
    @ApiModelProperty(value = "委托编号")
    private java.lang.String wtbh;
	/**设计强度*/
	@Excel(name = "设计强度", width = 15)
    @ApiModelProperty(value = "设计强度")
    private java.lang.String sjqd;
	/**试验日期*/
    @Excel(name = "试验日期，以天为单位", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "试验日期，以天为单位")
    private Date syrq;
	/**总盘数*/
	@Excel(name = "总盘数", width = 15)
    @ApiModelProperty(value = "总盘数")
    private java.lang.Integer allDish;
	/**总超标盘数*/
	@Excel(name = "总超标盘数", width = 15)
    @ApiModelProperty(value = "总超标盘数")
    private java.lang.Integer allOverproofDish;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
