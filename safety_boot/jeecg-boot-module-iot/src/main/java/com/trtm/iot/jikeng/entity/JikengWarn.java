package com.trtm.iot.jikeng.entity;

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
 * @Description: jikeng_warn
 * @Author: jeecg-boot
 * @Date:   2024-05-27
 * @Version: V1.0
 */
@Data
@TableName("jikeng_warn")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jikeng_warn对象", description="jikeng_warn")
public class JikengWarn implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String devsn;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String devname;
	/**预警地点*/
	@Excel(name = "预警地点", width = 15)
    @ApiModelProperty(value = "预警地点")
    private java.lang.String didian;
	/**检测项目*/
	@Excel(name = "检测项目", width = 15)
    @ApiModelProperty(value = "检测项目")
    private java.lang.String jiancexiangmu;
	/**支护类型*/
	@Excel(name = "支护类型", width = 15)
    @ApiModelProperty(value = "支护类型")
    private java.lang.String remark;
	/**时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "时间")
    private java.util.Date createTime;
	/**预警原因*/
	@Excel(name = "预警原因", width = 15)
    @ApiModelProperty(value = "预警原因")
    private java.lang.String note;
	/**预警级别*/
	@Excel(name = "预警级别", width = 15)
    @ApiModelProperty(value = "预警级别")
    private java.lang.String warnllevle;
	/**预警时间*/
	@Excel(name = "预警时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预警时间")
    private java.util.Date warnTime;
}
