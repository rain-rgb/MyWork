package com.trtm.iot.ydcx.entity;

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
 * @Description: w_yandu_s
 * @Author: jeecg-boot
 * @Date:   2022-12-09
 * @Version: V1.0
 */
@Data
@TableName("w_yandu_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="w_yandu_s对象", description="w_yandu_s")
public class WYanduS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**syjid*/
	@Excel(name = "syjid", width = 15)
    @ApiModelProperty(value = "syjid")
    private java.lang.String syjid;
	/**fxh*/
	@Excel(name = "fxh", width = 15)
    @ApiModelProperty(value = "fxh")
    private java.lang.String fxh;
	/**yandu1*/
	@Excel(name = "yandu1", width = 15)
    @ApiModelProperty(value = "yandu1")
    private java.lang.String yandu1;
	/**yandu2*/
	@Excel(name = "yandu2", width = 15)
    @ApiModelProperty(value = "yandu2")
    private java.lang.String yandu2;
	/**yandu3*/
	@Excel(name = "yandu3", width = 15)
    @ApiModelProperty(value = "yandu3")
    private java.lang.String yandu3;
	/**testtime*/
	@Excel(name = "testtime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "testtime")
    private java.util.Date testtime;
	/**fWtbh*/
	@Excel(name = "fWtbh", width = 15)
    @ApiModelProperty(value = "fWtbh")
    private java.lang.String fWtbh;
	/**fsbbh*/
	@Excel(name = "fsbbh", width = 15)
    @ApiModelProperty(value = "fsbbh")
    private java.lang.String fsbbh;
	/**fSjbh*/
	@Excel(name = "fSjbh", width = 15)
    @ApiModelProperty(value = "fSjbh")
    private java.lang.String fSjbh;
	/**isvalid*/
	@Excel(name = "isvalid", width = 15)
    @ApiModelProperty(value = "isvalid")
    private java.lang.String isvalid;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.String status;
	/**submittime*/
	@Excel(name = "submittime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "submittime")
    private java.util.Date submittime;
	/**张拉力*/
	@Excel(name = "张拉力", width = 15)
    @ApiModelProperty(value = "张拉力")
    private java.lang.String zll;
}
