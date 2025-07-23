package com.trtm.iot.gualanreal.entity;

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
 * @Description: 挂篮实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-11-16
 * @Version: V1.0
 */
@Data
@TableName("gualan_base_real")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gualan_base_real对象", description="挂篮实时数据表")
public class GualanBaseReal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibanhao;
	/**数据类标志*/
	@Excel(name = "数据类标志", width = 15)
    @ApiModelProperty(value = "数据类标志")
    private java.lang.Integer dataType;
	/**1#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)*/
	@Excel(name = "1#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)", width = 15)
    @ApiModelProperty(value = "1#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)")
    private java.lang.Double tension1;
	/**2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)*/
	@Excel(name = "2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)", width = 15)
    @ApiModelProperty(value = "2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)")
    private java.lang.Double tension2;
	/**3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)*/
	@Excel(name = "3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)", width = 15)
    @ApiModelProperty(value = "3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)")
    private java.lang.Double tension3;
	/**4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)*/
	@Excel(name = "4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)", width = 15)
    @ApiModelProperty(value = "4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)")
    private java.lang.Double tension4;
	/**下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)*/
	@Excel(name = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)", width = 15)
    @ApiModelProperty(value = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)")
    private java.lang.Double levelness1;
	/**下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)*/
	@Excel(name = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)", width = 15)
    @ApiModelProperty(value = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)")
    private java.lang.Double levelness2;
	/**1#螺纹钢是否超重  0  正常  1  超重*/
	@Excel(name = "1#螺纹钢是否超重  0  正常  1  超重", width = 15)
    @ApiModelProperty(value = "1#螺纹钢是否超重  0  正常  1  超重")
    @Dict(dicCode = "overweight")
    private java.lang.Integer overweight1;
	/**2#螺纹钢是否超重 0  正常  1  超重*/
	@Excel(name = "2#螺纹钢是否超重 0  正常  1  超重", width = 15)
    @ApiModelProperty(value = "2#螺纹钢是否超重 0  正常  1  超重")
    @Dict(dicCode = "overweight")
    private java.lang.Integer overweight2;
	/**3#螺纹钢是否超重 0  正常  1  超重*/
	@Excel(name = "3#螺纹钢是否超重 0  正常  1  超重", width = 15)
    @ApiModelProperty(value = "3#螺纹钢是否超重 0  正常  1  超重")
    @Dict(dicCode = "overweight")
    private java.lang.Integer overweight3;
	/**4#螺纹钢是否超重 0  正常 1  超重*/
	@Excel(name = "4#螺纹钢是否超重 0  正常 1  超重", width = 15)
    @ApiModelProperty(value = "4#螺纹钢是否超重 0  正常 1  超重")
    @Dict(dicCode = "overweight")
    private java.lang.Integer overweight4;
	/**挂篮前端下沉量（放大10倍单位mm）*/
	@Excel(name = "挂篮前端下沉量（放大10倍单位mm）", width = 15)
    @ApiModelProperty(value = "挂篮前端下沉量（放大10倍单位mm）")
    private java.lang.Double basket;
	/**风速（放大10倍 单位m/s）*/
	@Excel(name = "风速（放大10倍 单位m/s）", width = 15)
    @ApiModelProperty(value = "风速（放大10倍 单位m/s）")
    private java.lang.Double windspeed;
	/**风速报警0---正常  1---报警*/
	@Excel(name = "风速报警0---正常  1---报警", width = 15)
    @ApiModelProperty(value = "风速报警0---正常  1---报警")
    @Dict(dicCode = "windspeedwarn")
    private java.lang.Integer windspeedwarn;
	/**下托梁有效长度*/
	@Excel(name = "下托梁有效长度", width = 15)
    @ApiModelProperty(value = "下托梁有效长度")
    private java.lang.Double reservedOne;
	/**预留*/
	@Excel(name = "预留", width = 15)
    @ApiModelProperty(value = "预留")
    private java.lang.Integer reservedTwo;
	/**段结束符*/
	@Excel(name = "段结束符", width = 15)
    @ApiModelProperty(value = "段结束符")
    private java.lang.Integer sf;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
}
