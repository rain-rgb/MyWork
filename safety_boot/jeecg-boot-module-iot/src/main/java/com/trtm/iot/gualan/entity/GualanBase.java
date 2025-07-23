package com.trtm.iot.gualan.entity;

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
 * @Description: 挂篮数据主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Data
@TableName("gualan_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gualan_base对象", description="挂篮数据主表")
public class GualanBase implements Serializable {
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
    private java.lang.Float tension1;
    /**2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)*/
    @Excel(name = "2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)", width = 15)
    @ApiModelProperty(value = "2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)")
    private java.lang.Float tension2;
    /**3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)*/
    @Excel(name = "3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)", width = 15)
    @ApiModelProperty(value = "3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)")
    private java.lang.Float tension3;
    /**4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)*/
    @Excel(name = "4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)", width = 15)
    @ApiModelProperty(value = "4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)")
    private java.lang.Float tension4;
    /**下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)*/
    @Excel(name = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)", width = 15)
    @ApiModelProperty(value = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)")
    private java.lang.Float levelness1;
    /**下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)*/
    @Excel(name = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)", width = 15)
    @ApiModelProperty(value = "下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)")
    private java.lang.Float levelness2;
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
    private java.lang.Float basket;
    /**风速（放大10倍 单位m/s）*/
    @Excel(name = "风速（放大10倍 单位m/s）", width = 15)
    @ApiModelProperty(value = "风速（放大10倍 单位m/s）")
    private java.lang.Float windspeed;
    /**风速报警0---正常  1---报警*/
    @Excel(name = "风速报警0---正常  1---报警", width = 15)
    @Dict(dicCode = "windspeedwarn")
    @ApiModelProperty(value = "风速报警0---正常  1---报警")
    private java.lang.Integer windspeedwarn;
    /**下托梁有效长度*/
    @Excel(name = "下托梁有效长度", width = 15)
    @ApiModelProperty(value = "下托梁有效长度")
    private java.lang.Float reservedOne;
    /**预留*/
    @Excel(name = "预留", width = 15)
    @ApiModelProperty(value = "预留")
    private java.lang.Integer reservedTwo;
    /**段结束符*/
    @Excel(name = "段结束符", width = 15)
    @ApiModelProperty(value = "段结束符")
    private java.lang.Integer sf;
    /**螺纹钢承重预警额定拉力85%预警*/
    @Excel(name = "螺纹钢承重预警额定拉力85%预警", width = 15)
    @ApiModelProperty(value = "螺纹钢承重预警额定拉力85%预警")
    private java.lang.Integer overval1;
    /**超重预警额定重量100%预警*/
    @Excel(name = "超重预警额定重量100%预警", width = 15)
    @ApiModelProperty(value = "超重预警额定重量100%预警")
    private java.lang.Integer overval2;
    /**前横梁水平度预警偏差3度预警*/
    @Excel(name = "前横梁水平度预警偏差3度预警", width = 15)
    @ApiModelProperty(value = "前横梁水平度预警偏差3度预警")
    private java.lang.Integer overval3;
    /**横梁左右偏差3度预警倾斜预警偏差3度预警*/
    @Excel(name = "横梁左右偏差3度预警倾斜预警偏差3度预警", width = 15)
    @ApiModelProperty(value = "横梁左右偏差3度预警倾斜预警偏差3度预警")
    private java.lang.Integer overval4;
    /**纵梁变形预警偏差3度预警*/
    @Excel(name = "纵梁变形预警偏差3度预警", width = 15)
    @ApiModelProperty(value = "纵梁变形预警偏差3度预警")
    private java.lang.Integer overval5;
    /**数据时间*/
    @Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
    private Integer overproofStatus;//审核状态

}
