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
 * @Description: sy_m_param_cube
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Data
@TableName("sy_m_param_cube")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_m_param_cube对象", description="sy_m_param_cube")
public class SyMParamCube implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**cubeno*/
	@Excel(name = "cubeNo", width = 15)
    @ApiModelProperty(value = "cubeNo")
    @TableField(value = "cubeNo")
    private java.lang.String cubeNo;
	/**cubeqty*/
	@Excel(name = "cubeQty", width = 15)
    @ApiModelProperty(value = "cubeQty")
    @TableField(value = "cubeQty")
    private java.lang.String cubeQty;
	/**cubevol*/
	@Excel(name = "cubeVol", width = 15)
    @ApiModelProperty(value = "cubeVol")
    @TableField(value = "cubeVol")
    private java.lang.String cubeVol;
	/**departid*/
	@Excel(name = "orgCode", width = 15)
    @ApiModelProperty(value = "orgCode")
    @TableField(value = "orgCode")
    private java.lang.String orgCode;
	/**cubeOrder*/
	@Excel(name = "cubeOrder", width = 15)
    @ApiModelProperty(value = "cubeOrder")
    @TableField(value = "cubeOrder")
    private java.lang.Integer cubeOrder;
}
