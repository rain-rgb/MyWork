package com.trtm.iot.hntconsignpic.entity;

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
 * @Description: 混凝土见证取样样品图表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
@Data
@TableName("hnt_consign_pic")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hnt_consign_pic对象", description="混凝土见证取样样品图表信息")
public class HntConsignPic implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**委托ID--guid*/
	@Excel(name = "委托ID--guid", width = 15)
    @ApiModelProperty(value = "委托ID--guid")
    private java.lang.String wtid;
	/**委托编号*/
	@Excel(name = "委托编号", width = 15)
    @ApiModelProperty(value = "委托编号")
    private java.lang.String wtbh;
	/**取样图片服务器地址1*/
	@Excel(name = "取样图片服务器地址1", width = 15)
    @ApiModelProperty(value = "取样图片服务器地址1")
    private java.lang.String wtpic1;
	/**取样图片服务器地址2*/
	@Excel(name = "取样图片服务器地址2", width = 15)
    @ApiModelProperty(value = "取样图片服务器地址2")
    private java.lang.String wtpic2;
	/**取样图片服务器地址3*/
	@Excel(name = "取样图片服务器地址3", width = 15)
    @ApiModelProperty(value = "取样图片服务器地址3")
    private java.lang.String wtpic3;
	/**取样图片服务器地址4*/
	@Excel(name = "取样图片服务器地址4", width = 15)
    @ApiModelProperty(value = "取样图片服务器地址4")
    private java.lang.String wtpic4;
}
