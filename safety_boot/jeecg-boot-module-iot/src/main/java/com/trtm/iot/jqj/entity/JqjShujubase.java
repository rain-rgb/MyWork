package com.trtm.iot.jqj.entity;

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
 * @Description: jqj_shujubase
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Data
@TableName("jqj_shujubase")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jqj_shujubase对象", description="jqj_shujubase")
public class JqjShujubase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String deviceCode;
	/**数据类标志*/
	@Excel(name = "数据类标志", width = 15)
    @ApiModelProperty(value = "数据类标志")
    private Integer dataType;
	/**大车横向行程(放大100倍)*/
	@Excel(name = "大车横向行程(放大100倍)", width = 15)
    @ApiModelProperty(value = "大车横向行程(放大100倍)")
    private Integer bigCrosswise;
	/**大车纵向行程（放大100倍）*/
	@Excel(name = "大车纵向行程（放大100倍）", width = 15)
    @ApiModelProperty(value = "大车纵向行程（放大100倍）")
    private Integer bigPortrait;
	/**支腿垂直度(放大100倍)*/
	@Excel(name = "支腿垂直度(放大100倍)", width = 15)
    @ApiModelProperty(value = "支腿垂直度(放大100倍)")
    private Integer legPerpendicularity;
	/**大车水平度(放大100倍)*/
	@Excel(name = "大车水平度(放大100倍)", width = 15)
    @ApiModelProperty(value = "大车水平度(放大100倍)")
    private Integer bigLevel;
	/**1#天车高度(放大100倍)*/
	@Excel(name = "1#天车高度(放大100倍)", width = 15)
    @ApiModelProperty(value = "1#天车高度(放大100倍)")
    private Integer skyHeight1;
	/**1#天车吊重（放大100倍）*/
	@Excel(name = "1#天车吊重（放大100倍）", width = 15)
    @ApiModelProperty(value = "1#天车吊重（放大100倍）")
    private Integer skyWeight1;
	/**1#天车横向行程（）*/
	@Excel(name = "1#天车横向行程（）", width = 15)
    @ApiModelProperty(value = "1#天车横向行程（）")
    private Double skyCrosswise1;
	/**1#天车纵向行程（）*/
	@Excel(name = "1#天车纵向行程（）", width = 15)
    @ApiModelProperty(value = "1#天车纵向行程（）")
    private Double skyPortrait1;
	/**1#天车吊钩载荷状态
0	重量正常
1   重量超载*/
	@Excel(name = "1#天车吊钩载荷状态")
    private java.lang.Integer skyLoad;
	/**1#天车吊钩制动器1状态
0	制动器闭合
1   制动器释放*/
    private java.lang.Integer skyBraking11;
}
