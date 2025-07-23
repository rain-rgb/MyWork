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
 * @Description: sy_m_param_boxqty
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Data
@TableName("sy_m_param_boxqty")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_m_param_boxqty对象", description="sy_m_param_boxqty")
public class SyMParamBoxqty implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**boxno*/
	@Excel(name = "boxNo", width = 15)
    @ApiModelProperty(value = "boxNo")
    @TableField(value = "boxNo")
    private java.lang.String boxNo;
	/**boxqty*/
	@Excel(name = "boxQty", width = 15)
    @ApiModelProperty(value = "boxQty")
    @TableField(value = "boxQty")
    private java.lang.String boxQty;
	/**departid*/
	@Excel(name = "orgCode", width = 15)
    @ApiModelProperty(value = "orgCode")
    @TableField(value = "orgCode")
    private java.lang.String orgCode;
}
