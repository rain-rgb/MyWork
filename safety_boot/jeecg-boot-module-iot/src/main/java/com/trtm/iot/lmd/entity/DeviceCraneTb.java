package com.trtm.iot.lmd.entity;

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
 * @Description: device_crane_tb
 * @Author: jeecg-boot
 * @Date:   2025-04-28
 * @Version: V1.0
 */
@Data
@TableName("device_crane_tb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_crane_tb对象", description="device_crane_tb")
public class DeviceCraneTb implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**台座值*/
	@Excel(name = "台座值", width = 15)
    @ApiModelProperty(value = "台座值")
    private java.lang.String tb;
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private java.lang.String code;
	/**存梁名称*/
	@Excel(name = "存梁名称", width = 15)
    @ApiModelProperty(value = "存梁名称")
    private java.lang.String name;
	/**z组织机构*/
    @ApiModelProperty(value = "z组织机构")
    private java.lang.String sysOrgCode;
}
