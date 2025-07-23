package com.trtm.iot.hc_constructionresults_ny.entity;

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
 * @Description: 施工成果碾压
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("hc_constructionresults_ny")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_constructionresults_ny对象", description="施工成果碾压")
public class HcConstructionresultsNy implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**碾压机械类型，steel钢轮，tyre胶轮*/
	@Excel(name = "碾压机械类型，steel钢轮，tyre胶轮", width = 15)
    @ApiModelProperty(value = "碾压机械类型，steel钢轮，tyre胶轮")
    private java.lang.String vehicletype;
	/**温度正常占比*/
	@Excel(name = " 温度正常占比", width = 15)
    @ApiModelProperty(value = " 温度正常占比")
    private java.lang.String tempratio;
	/**平均碾压速度（m/s）*/
	@Excel(name = "平均碾压速度（m/s）", width = 15)
    @ApiModelProperty(value = "平均碾压速度（m/s）")
    private java.lang.String avgvelocity;
	/**速度正常占比*/
	@Excel(name = " 速度正常占比", width = 15)
    @ApiModelProperty(value = " 速度正常占比")
    private java.lang.String velocityratio;
	/**碾压遍数正常占比*/
	@Excel(name = "碾压遍数正常占比", width = 15)
    @ApiModelProperty(value = "碾压遍数正常占比")
    private java.lang.String timesratio;
	/**平均遍数*/
	@Excel(name = "平均遍数", width = 15)
    @ApiModelProperty(value = "平均遍数")
    private java.lang.String avgtimes;
    private java.lang.String totaltimesratio;
	/**主表id*/
	@Excel(name = "主表id", width = 15)
    @ApiModelProperty(value = "主表id")
    private java.lang.String crid;
}
