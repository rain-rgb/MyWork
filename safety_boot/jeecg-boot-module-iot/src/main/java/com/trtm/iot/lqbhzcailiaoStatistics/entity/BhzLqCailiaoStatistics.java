package com.trtm.iot.lqbhzcailiaoStatistics.entity;

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
 * @Description: bhz_lq_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Data
@TableName("bhz_lq_cailiao_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_lq_cailiao_statistics对象", description="bhz_lq_cailiao_statistics")
public class BhzLqCailiaoStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**与沥青站生产数据统计表（bhz_lq_statistics）的id*/
	@Excel(name = "与沥青站生产数据统计表（bhz_lq_statistics）的id", width = 15)
    @ApiModelProperty(value = "与沥青站生产数据统计表（bhz_lq_statistics）的id")
    private java.lang.Integer csId;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private java.lang.String cailiaoid;
	/**材料类别*/
	@Excel(name = "材料类别", width = 15)
    @ApiModelProperty(value = "材料类别")
    private java.lang.Integer materialeType;
	/**材料名*/
	@Excel(name = "材料名", width = 15)
    @ApiModelProperty(value = "材料名")
    private java.lang.String materialeName;
	/**这种材料真实用量总和*/
	@Excel(name = "这种材料真实用量总和", width = 15)
    @ApiModelProperty(value = "这种材料真实用量总和")
    private java.lang.Double realityNumber;
	/**这种材料的理论用量总和*/
	@Excel(name = "这种材料的理论用量总和", width = 15)
    @ApiModelProperty(value = "这种材料的理论用量总和")
    private java.lang.Double theoryNumber;
	/**初级超标次数*/
	@Excel(name = "初级超标次数", width = 15)
    @ApiModelProperty(value = "初级超标次数")
    private java.lang.Integer primaryNumber;
	/**中极超标次数*/
	@Excel(name = "中极超标次数", width = 15)
    @ApiModelProperty(value = "中极超标次数")
    private java.lang.Integer middleNumber;
	/**高级超标次数*/
	@Excel(name = "高级超标次数", width = 15)
    @ApiModelProperty(value = "高级超标次数")
    private java.lang.Integer advancedNumber;
}
