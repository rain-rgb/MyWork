package com.trtm.iot.kongjingtongdao.entity;

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
 * @Description: 通道参数信息表
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@Data
@TableName("kongjing_tongdao")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="kongjing_tongdao对象", description="通道参数信息表")
public class KongjingTongdao implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
	/**通道号 0、1、2、3（其依次对 应X、X'、Y、Y'）*/
	@Excel(name = "通道号 0、1、2、3（其依次对 应X、X'、Y、Y'）", width = 15)
    @ApiModelProperty(value = "通道号 0、1、2、3（其依次对 应X、X'、Y、Y'）")
    @Dict(dicCode = "channelnum")
    private java.lang.Integer channelnum;
	/**状态 0：未启用 1：启用并 显示*/
	@Excel(name = "状态 0：未启用 1：启用并 显示", width = 15)
    @ApiModelProperty(value = "状态 0：未启用 1：启用并 显示")
    @Dict(dicCode = "filter")
    private java.lang.Integer status;
	/**中心距 单位mm*/
	@Excel(name = "中心距 单位mm", width = 15)
    @ApiModelProperty(value = "中心距 单位mm")
    private java.lang.Integer centerdistance;
	/**增益*/
	@Excel(name = "增益", width = 15)
    @ApiModelProperty(value = "增益")
    private java.lang.Integer gain;
	/**延时 单位us*/
	@Excel(name = "延时 单位us", width = 15)
    @ApiModelProperty(value = "延时 单位us")
    private java.lang.Double delay;
	/**校零时间 单位us*/
	@Excel(name = "校零时间 单位us", width = 15)
    @ApiModelProperty(value = "校零时间 单位us")
    private java.lang.Double zerotime;
}
