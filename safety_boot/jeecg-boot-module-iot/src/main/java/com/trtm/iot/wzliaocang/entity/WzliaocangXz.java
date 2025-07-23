package com.trtm.iot.wzliaocang.entity;

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
 * @Description: wzliaocang_xz
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Data
@TableName("wzliaocang_xz")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzliaocang_xz对象", description="wzliaocang_xz")
public class WzliaocangXz implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**物质料仓唯一id*/
	@Excel(name = "物质料仓唯一id", width = 15)
    @ApiModelProperty(value = "物质料仓唯一id")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private java.lang.String guid;
	/**修正人*/
    @ApiModelProperty(value = "修正人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
	/**修正值*/
	@Excel(name = "修正值", width = 15)
    @ApiModelProperty(value = "修正值")
    private java.lang.Double updateValue;
	/**修正时间*/
    @ApiModelProperty(value = "修正时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private java.lang.String updateTime;
	/**修正原因*/
	@Excel(name = "修正原因", width = 15)
    @ApiModelProperty(value = "修正原因")
    private java.lang.String remark;
}
