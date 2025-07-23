package com.trtm.iot.pushandreturn.entity;

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
 * @Description: 接口推送信息
 * @Author: jeecg-boot
 * @Date:   2023-05-19
 * @Version: V1.0
 */
@Data
@TableName("pushandreturn")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pushandreturn对象", description="接口推送信息")
public class Pushandreturn implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**推送数据id*/
	@Excel(name = "推送数据id", width = 15)
    @ApiModelProperty(value = "推送数据id")
    private java.lang.String pushdataid;
	/**推送名称*/
	@Excel(name = "推送名称", width = 15)
    @ApiModelProperty(value = "推送名称")
    private java.lang.String pushname;
	/**推送数据*/
	@Excel(name = "推送数据", width = 15)
    @ApiModelProperty(value = "推送数据")
    private java.lang.String pushjson;
	/**返回值*/
	@Excel(name = "返回值", width = 15)
    @ApiModelProperty(value = "返回值")
    private java.lang.String returnvalue;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "推送时间")
    private Date pushdate;
}
