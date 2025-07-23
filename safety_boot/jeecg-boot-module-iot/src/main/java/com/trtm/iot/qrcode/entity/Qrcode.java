package com.trtm.iot.qrcode.entity;

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
 * @Description: 二维码数据表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
@Data
@TableName("qrcode")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="qrcode对象", description="二维码数据表信息")
public class Qrcode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**uuid*/
	@Excel(name = "uuid", width = 15)
    @ApiModelProperty(value = "uuid")
    private java.lang.String uuid;
	/**stamppath*/
	@Excel(name = "stamppath", width = 15)
    @ApiModelProperty(value = "stamppath")
    private java.lang.String stamppath;
	/**createtime*/
	@Excel(name = "createtime", width = 15)
    @ApiModelProperty(value = "createtime")
    private java.lang.String createtime;
	/**imgname*/
	@Excel(name = "imgname", width = 15)
    @ApiModelProperty(value = "imgname")
    private java.lang.String imgname;
	/**isdown*/
	@Excel(name = "isdown", width = 15)
    @ApiModelProperty(value = "isdown")
    private java.lang.String isdown;
	/**number*/
	@Excel(name = "number", width = 15)
    @ApiModelProperty(value = "number")
    private java.lang.Integer number;
	/**ts*/
	@Excel(name = "ts", width = 15)
    @ApiModelProperty(value = "ts")
    private java.lang.Long ts;
}
