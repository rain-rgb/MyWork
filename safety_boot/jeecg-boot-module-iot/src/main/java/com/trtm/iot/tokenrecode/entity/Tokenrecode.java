package com.trtm.iot.tokenrecode.entity;

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
 * @Description: tokenrecode
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@TableName("tokenrecode")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tokenrecode对象", description="tokenrecode")
public class Tokenrecode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
	/**key*/
	@Excel(name = "key", width = 15)
    @ApiModelProperty(value = "key")
    private java.lang.String rediskey;
	/**value*/
	@Excel(name = "value", width = 15)
    @ApiModelProperty(value = "value")
    private java.lang.String redisvalue;
	/**sysOrgCode*/
    @ApiModelProperty(value = "sysOrgCode")
    private java.lang.String sysOrgCode;
	/**username*/
	@Excel(name = "username", width = 15)
    @ApiModelProperty(value = "username")
    private java.lang.String username;
	/**del*/
	@Excel(name = "del", width = 15)
    @ApiModelProperty(value = "del")
    private java.lang.Integer del;

    private java.lang.String httpurl;
    private java.lang.String params;
}
