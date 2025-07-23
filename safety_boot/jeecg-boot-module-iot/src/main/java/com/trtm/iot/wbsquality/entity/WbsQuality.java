package com.trtm.iot.wbsquality.entity;

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
 * @Description: wbs_quality
 * @Author: jeecg-boot
 * @Date:   2024-11-27
 * @Version: V1.0
 */
@Data
@TableName("wbs_quality")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wbs_quality对象", description="wbs_quality")
public class WbsQuality implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**wbs 结构类型*/
	@Excel(name = "wbs 结构类型", width = 15)
    @ApiModelProperty(value = "wbs 结构类型")
    private String wbsStructureType;
	/**模板id*/
	@Excel(name = "模板id", width = 15)
    @ApiModelProperty(value = "模板id")
    private String templateid;
	/**质检单编号*/
	@Excel(name = "质检单编号", width = 15)
    @ApiModelProperty(value = "质检单编号")
    private String reportNumber;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;
	/**reportName*/
	@Excel(name = "reportName", width = 15)
    @ApiModelProperty(value = "reportName")
    private String reportName;
	/**order*/
	@Excel(name = "order", width = 15)
    @ApiModelProperty(value = "order")
    private Integer orders;
	/**parentnote*/
	@Excel(name = "parentnote", width = 15)
    @ApiModelProperty(value = "parentnote")
    private String parentnote;
}
