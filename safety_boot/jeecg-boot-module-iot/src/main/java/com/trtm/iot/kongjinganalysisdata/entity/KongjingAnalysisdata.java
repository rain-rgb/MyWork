package com.trtm.iot.kongjinganalysisdata.entity;

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
 * @Description: 孔径分析数据
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@Data
@TableName("kongjing_analysisdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="kongjing_analysisdata对象", description="孔径分析数据")
public class KongjingAnalysisdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**基本信息 id*/
	@Excel(name = "基本信息 id", width = 15)
    @ApiModelProperty(value = "基本信息 id")
    private java.lang.String basicinfoid;
	/** 设计井深 单位 m*/
	@Excel(name = " 设计井深 单位 m", width = 15)
    @ApiModelProperty(value = " 设计井深 单位 m")
    private java.lang.Double designdepth;
	/** 设计孔径 单位 mm*/
	@Excel(name = " 设计孔径 单位 mm", width = 15)
    @ApiModelProperty(value = " 设计孔径 单位 mm")
    private java.lang.Double designdiameter;
	/**最大孔径 单位 mm*/
	@Excel(name = "最大孔径 单位 mm", width = 15)
    @ApiModelProperty(value = "最大孔径 单位 mm")
    private java.lang.Double maxdiameter;
	/**最小孔径 单位mm*/
	@Excel(name = "最小孔径 单位mm", width = 15)
    @ApiModelProperty(value = "最小孔径 单位mm")
    private java.lang.Double mindiameter;
	/**平均孔径 单位 mm*/
	@Excel(name = "平均孔径 单位 mm", width = 15)
    @ApiModelProperty(value = "平均孔径 单位 mm")
    private java.lang.Double avgdiameter;
	/**偏心距 单位mm*/
	@Excel(name = "偏心距 单位mm", width = 15)
    @ApiModelProperty(value = "偏心距 单位mm")
    private java.lang.Double eccentricity;
	/** 沉渣厚度  单位m*/
	@Excel(name = " 沉渣厚度  单位m", width = 15)
    @ApiModelProperty(value = " 沉渣厚度  单位m")
    private java.lang.Double thickness;
	/**垂直度 %*/
	@Excel(name = "垂直度 %", width = 15)
    @ApiModelProperty(value = "垂直度 %")
    private java.lang.Double verticality;
}
