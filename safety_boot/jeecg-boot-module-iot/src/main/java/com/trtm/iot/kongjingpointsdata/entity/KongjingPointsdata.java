package com.trtm.iot.kongjingpointsdata.entity;

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
 * @Description: 孔径通道采样数据
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@Data
@TableName("kongjing_pointsdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="kongjing_pointsdata对象", description="孔径通道采样数据")
public class KongjingPointsdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**基本信息 id*/
	@Excel(name = "基本信息 id", width = 15)
    @ApiModelProperty(value = "基本信息 id")
    private java.lang.String basicinfoid;
	/**通道号 0、1、2、3（其依次对 应 X、X'、Y、Y'）*/
	@Excel(name = "通道号 0、1、2、3（其依次对 应 X、X'、Y、Y'）", width = 15)
    @ApiModelProperty(value = "通道号 0、1、2、3（其依次对 应 X、X'、Y、Y'）")
    @Dict(dicCode = "channelnum")
    private java.lang.Integer passway;
	/**深度 单位 m*/
	@Excel(name = "深度 单位 m", width = 15)
    @ApiModelProperty(value = "深度 单位 m")
    private java.lang.Double depth;
	/**声时 单位 us*/
	@Excel(name = "声时 单位 us", width = 15)
    @ApiModelProperty(value = "声时 单位 us")
    private java.lang.Double soundtime;
	/**泥浆声速 单位km/s*/
	@Excel(name = "泥浆声速 单位km/s", width = 15)
    @ApiModelProperty(value = "泥浆声速 单位km/s")
    private java.lang.Double soundspeed;
	/**波幅*/
	@Excel(name = "波幅", width = 15)
    @ApiModelProperty(value = "波幅")
    private java.lang.Double amplitude;
	/**方位角*/
	@Excel(name = "方位角", width = 15)
    @ApiModelProperty(value = "方位角")
    private java.lang.Double azimuth;
	/**半径 单位mm*/
	@Excel(name = "半径 单位mm", width = 15)
    @ApiModelProperty(value = "半径 单位mm")
    private java.lang.Integer radius;
	/**增益 */
	@Excel(name = "增益 ", width = 15)
    @ApiModelProperty(value = "增益 ")
    private java.lang.Double gain;
	/**延时 单位us*/
	@Excel(name = "延时 单位us", width = 15)
    @ApiModelProperty(value = "延时 单位us")
    private java.lang.Double delay;
	/**增强声时 单位us*/
	@Excel(name = "增强声时 单位us", width = 15)
    @ApiModelProperty(value = "增强声时 单位us")
    private java.lang.Double enhancetime;
	/**修正距离 单位mm*/
	@Excel(name = "修正距离 单位mm", width = 15)
    @ApiModelProperty(value = "修正距离 单位mm")
    private java.lang.Double correctiondistance;
	/**修正方向 0:上 90:左 180:下 270: 右*/
	@Excel(name = "修正方向 0:上 90:左 180:下 270: 右", width = 15)
    @ApiModelProperty(value = "修正方向 0:上 90:左 180:下 270: 右")
    @Dict(dicCode = "correctiondirection")
    private java.lang.Integer correctiondirection;
	/**噪声档位 范围[0-40]每档 50us 间 隔*/
	@Excel(name = "噪声档位 范围[0-40]每档 50us 间 隔", width = 15)
    @ApiModelProperty(value = "噪声档位 范围[0-40]每档 50us 间隔")
    private java.lang.Integer adjustinggear;
	/**测点时间 记录离始测点的时间; 始测点为0s ; 单位秒*/
	@Excel(name = "测点时间 记录离始测点的时间; 始测点为0s ; 单位秒", width = 15)
    @ApiModelProperty(value = "测点时间 记录离始测点的时间; 始测点为0s ; 单位秒")
    private java.lang.Integer testtime;
	/**数据  波形数值*/
	@Excel(name = "数据  波形数值", width = 15)
    @ApiModelProperty(value = "数据  波形数值")
    private java.lang.String wavedata;
}
