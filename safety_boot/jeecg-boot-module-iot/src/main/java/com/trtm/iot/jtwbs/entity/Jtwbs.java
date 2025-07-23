package com.trtm.iot.jtwbs.entity;

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
 * @Description: jtwbs
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Data
@TableName("jtwbs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jtwbs对象", description="jtwbs")
public class Jtwbs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**projectid*/
	@Excel(name = "projectid", width = 15)
    @ApiModelProperty(value = "projectid")
    private java.lang.String projectid;
	/**sectionid*/
	@Excel(name = "sectionid", width = 15)
    @ApiModelProperty(value = "sectionid")
    private java.lang.String sectionid;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private java.lang.String name;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;
	/**info*/
	@Excel(name = "info", width = 15)
    @ApiModelProperty(value = "info")
    private java.lang.String info;
	/**0 需要更新；1不需要更新*/
	@Excel(name = "0 需要更新；1不需要更新", width = 15)
    @ApiModelProperty(value = "0 需要更新；1不需要更新")
    private java.lang.Integer isupdata;
	/**sysOrgCode*/
    @ApiModelProperty(value = "sysOrgCode")
    private java.lang.String sysOrgCode;

    private java.lang.String username;
}
