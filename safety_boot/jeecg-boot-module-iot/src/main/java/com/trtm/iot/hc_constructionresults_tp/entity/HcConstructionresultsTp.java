package com.trtm.iot.hc_constructionresults_tp.entity;

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
 * @Description: 施工成果摊铺
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("hc_constructionresults_tp")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_constructionresults_tp对象", description="施工成果摊铺")
public class HcConstructionresultsTp implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**厚度*/
	@Excel(name = "厚度", width = 15)
    @ApiModelProperty(value = "厚度")
    private java.lang.String avgthick;
	/**温度（°C）*/
	@Excel(name = "温度（°C）", width = 15)
    @ApiModelProperty(value = "温度（°C）")
    private java.lang.String avgtemp;
	/**温度离析*/
	@Excel(name = " 温度离析", width = 15)
    @ApiModelProperty(value = " 温度离析")
    private java.lang.String avgtempdiff;
	/**平均摊铺速度（m/s）*/
	@Excel(name = "平均摊铺速度（m/s）", width = 15)
    @ApiModelProperty(value = "平均摊铺速度（m/s）")
    private java.lang.String avgvelocity;
	/**速度正常占比*/
	@Excel(name = " 速度正常占比", width = 15)
    @ApiModelProperty(value = " 速度正常占比")
    private java.lang.String velocityratio;
	/**虚铺厚度*/
	@Excel(name = "虚铺厚度", width = 15)
    @ApiModelProperty(value = "虚铺厚度")
    private java.lang.String avgloosedepth;
	/**主表id*/
	@Excel(name = "主表id", width = 15)
    @ApiModelProperty(value = "主表id")
    private java.lang.String crid;
}
