package com.trtm.sy.sycspz.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: sy_m_param_ratio
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Data
@TableName("sy_m_param_ratio")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_m_param_ratio对象", description="sy_m_param_ratio")
public class SyMParamRatio implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**gatheruse*/
	@Excel(name = "gatherUse", width = 15)
    @ApiModelProperty(value = "gatherUse")
    @TableField(value = "gatherUse")
    private java.lang.String gatherUse;
	/**mixtype*/
	@Excel(name = "mixType", width = 15)
    @ApiModelProperty(value = "mixType")
    @TableField(value = "mixType")
    private java.lang.String mixType;
	/**holesize*/
	@Excel(name = "holeSize", width = 15)
    @ApiModelProperty(value = "holeSize")
    @TableField(value = "holeSize")
    private java.lang.String holeSize;
	/**rangeup*/
	@Excel(name = "rangeUp", width = 15)
    @ApiModelProperty(value = "rangeUp")
    @TableField(value = "rangeUp")
    private java.lang.String rangeUp;
	/**rangedown*/
	@Excel(name = "rangeDown", width = 15)
    @ApiModelProperty(value = "rangeDown")
    @TableField(value = "rangeDown")
    private java.lang.String rangeDown;
	/**holesizen*/
	@Excel(name = "holeSizeN", width = 15)
    @ApiModelProperty(value = "holeSizeN")
    @TableField(value = "holeSizeN")
    private java.lang.String holeSizeN;
    @Excel(name = "orgCode", width = 15)
    @ApiModelProperty(value = "orgCode")
    @TableField(value = "orgCode")
    private java.lang.String orgCode;
}
