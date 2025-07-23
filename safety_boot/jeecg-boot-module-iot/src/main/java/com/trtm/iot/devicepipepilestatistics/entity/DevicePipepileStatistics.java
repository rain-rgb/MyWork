package com.trtm.iot.devicepipepilestatistics.entity;

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
 * @Description: device_pipepile_statistics
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
@Data
@TableName("device_pipepile_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_pipepile_statistics对象", description="device_pipepile_statistics")
public class DevicePipepileStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String orgcode;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称")
    private java.lang.String utilname;
	/**里程名称*/
	@Excel(name = "里程名称", width = 15)
    @ApiModelProperty(value = "里程名称")
    private java.lang.String mileage;
	/**桩基数（根）*/
	@Excel(name = "桩基数（根）", width = 15)
    @ApiModelProperty(value = "桩基数（根）")
    private java.lang.String pilecount;
	/**里程id*/
	@Excel(name = "里程id", width = 15)
    @ApiModelProperty(value = "里程id")
    private java.lang.Integer mileageid;
	/**施工总长度（m）*/
	@Excel(name = "施工总长度（m）", width = 15)
    @ApiModelProperty(value = "施工总长度（m）")
    private java.lang.String worklength;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeino;
	/**超标数*/
	@Excel(name = "超标数", width = 15)
    @ApiModelProperty(value = "超标数")
    private java.lang.Integer chaobiaototal;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**统计日期*/
	@Excel(name = "统计日期", width = 15)
    @ApiModelProperty(value = "统计日期")
    private java.lang.String stadate;
}
