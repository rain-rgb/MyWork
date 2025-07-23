package com.trtm.iot.hc_project.entity;

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
 * @Description: 华测获取项目
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Data
@TableName("hc_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_project对象", description="华测获取项目")
public class HcProject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**唯一id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一id")
    private java.lang.Integer id;
	/**工程ID*/
	@Excel(name = "工程ID", width = 15)
    @ApiModelProperty(value = "工程ID")
    private java.lang.String pjid;
	/**工程名*/
	@Excel(name = "工程名", width = 15)
    @ApiModelProperty(value = "工程名")
    private java.lang.String pjname;
	/**桩号规范，K4+398或者DK4+398*/
	@Excel(name = "桩号规范，K4+398或者DK4+398", width = 15)
    @ApiModelProperty(value = "桩号规范，K4+398或者DK4+398")
    private java.lang.String pjstationprefix;
	/**项目描述*/
	@Excel(name = "项目描述", width = 15)
    @ApiModelProperty(value = "项目描述")
    private java.lang.String pjdescription;
	/**项目开始施工时间*/
	@Excel(name = "项目开始施工时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "项目开始施工时间")
    private java.util.Date pjstartdate;
	/**项目形状类型，0：线型，1：面状*/
	@Excel(name = "项目形状类型，0：线型，1：面状", width = 15)
    @ApiModelProperty(value = "项目形状类型，0：线型，1：面状")
    private java.lang.String pjshapetype;
	/**项目业主单位id*/
	@Excel(name = "项目业主单位id", width = 15)
    @ApiModelProperty(value = "项目业主单位id")
    private java.lang.String pjownercompanyid;
	/**项目业主单位*/
	@Excel(name = "项目业主单位", width = 15)
    @ApiModelProperty(value = "项目业主单位")
    private java.lang.String pjownercompany;
	/**对应的项目编码*/
	@Excel(name = "对应的项目编码", width = 15)
    @ApiModelProperty(value = "对应的项目编码")
    private java.lang.String orgcode;
	/**备用*/
	@Excel(name = "备用", width = 15)
    @ApiModelProperty(value = "备用")
    private java.lang.String beiyong;
}
