package com.trtm.iot.cofferdam.entity;

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
 * @Description: cofferdam_realdata
 * @Author: jeecg-boot
 * @Date:   2024-12-21
 * @Version: V1.0
 */
@Data
@TableName("cofferdam_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cofferdam_realdata对象", description="cofferdam_realdata")
public class CofferdamRealdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**围堰实时数据id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "围堰实时数据id")
    private java.lang.Integer id;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projName;
	/**测点名称*/
	@Excel(name = "测点名称", width = 15)
    @ApiModelProperty(value = "测点名称")
    private java.lang.String cedName;
	/**监测类型*/
	@Excel(name = "监测类型", width = 15)
    @ApiModelProperty(value = "监测类型")
    private java.lang.String jcTypeName;
	/**分组名称*/
	@Excel(name = "分组名称", width = 15)
    @ApiModelProperty(value = "分组名称")
    private java.lang.String fenzName;
	/**测点id*/
	@Excel(name = "测点id", width = 15)
    @ApiModelProperty(value = "测点id")
    private java.lang.Integer cedId;
	/**测点key*/
	@Excel(name = "测点key", width = 15)
    @ApiModelProperty(value = "测点key")
    private java.lang.String cedKey;
	/**监测类型编码*/
	@Excel(name = "监测类型编码", width = 15)
    @ApiModelProperty(value = "监测类型编码")
    private java.lang.String jcTypeCode;
	/**最新数据时间*/
	@Excel(name = "最新数据时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "最新数据时间")
    private java.util.Date datatime;
	/**最新数据*/
	@Excel(name = "最新数据", width = 15)
    @ApiModelProperty(value = "最新数据")
    private java.lang.String datajson;
	/**测点状态*/
	@Excel(name = "测点状态", width = 15)
    @ApiModelProperty(value = "测点状态")
    private java.lang.String cedStatus;
	/**数据来源*/
	@Excel(name = "数据来源", width = 15)
    @ApiModelProperty(value = "数据来源")
    private java.lang.String dataSource;
	/**测点缩略图*/
	@Excel(name = "测点缩略图", width = 15)
    @ApiModelProperty(value = "测点缩略图")
    private java.lang.String cedPic;
}
