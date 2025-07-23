package com.trtm.iot.device_concrete_realdata.entity;

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
 * @Description: 混凝土成熟度监测
 * @Author: jeecg-boot
 * @Date:   2024-10-22
 * @Version: V1.0
 */
@Data
@TableName("device_concrete_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_concrete_realdata对象", description="混凝土成熟度监测")
public class DeviceConcreteRealdata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**施工地址*/
	@Excel(name = "施工地址", width = 15)
    @ApiModelProperty(value = "施工地址")
    private java.lang.String sgdz;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String gcmc;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String xmmc;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String sbbh;
	/**数据更新时间*/
	@Excel(name = "数据更新时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据更新时间")
    private java.util.Date updatetime;
	/**浇筑时间*/
	@Excel(name = "浇筑时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇筑时间")
    private java.util.Date jiaozhudate;
	/**龄期（天）*/
	@Excel(name = "龄期（天）", width = 15)
    @ApiModelProperty(value = "龄期（天）")
    private java.lang.String lq;
	/**等效龄期（天）*/
	@Excel(name = "等效龄期（天）", width = 15)
    @ApiModelProperty(value = "等效龄期（天）")
    private java.lang.String equivalentlq;
	/**外置等效龄期（天）*/
	@Excel(name = "外置等效龄期（天）", width = 15)
    @ApiModelProperty(value = "外置等效龄期（天）")
    private java.lang.String equivalentlqoutside;
	/**成熟度（°C*天）*/
	@Excel(name = "成熟度（°C*天）", width = 15)
    @ApiModelProperty(value = "成熟度（°C*天）")
    private java.lang.String maturity;
	/**外置成熟度（°C*天）*/
	@Excel(name = "外置成熟度（°C*天）", width = 15)
    @ApiModelProperty(value = "外置成熟度（°C*天）")
    private java.lang.String maturityoutside;
	/**内置温度（°C）*/
	@Excel(name = "内置温度（°C）", width = 15)
    @ApiModelProperty(value = "内置温度（°C）")
    private java.lang.String temperature;
	/**外置温度（°C）*/
	@Excel(name = "外置温度（°C）", width = 15)
    @ApiModelProperty(value = "外置温度（°C）")
    private java.lang.String temperatureoutside;
	/**强度（Mpa）*/
	@Excel(name = "强度（Mpa）", width = 15)
    @ApiModelProperty(value = "强度（Mpa）")
    private java.lang.String strength;
	/**外置强度（Mpa）*/
	@Excel(name = "外置强度（Mpa）", width = 15)
    @ApiModelProperty(value = "外置强度（Mpa）")
    private java.lang.String strengthoutside;
	/**时间过程值*/
	@Excel(name = "时间过程值", width = 15)
    @ApiModelProperty(value = "时间过程值")
    private java.lang.String time;
	/**成熟度过程值*/
	@Excel(name = "成熟度过程值", width = 15)
    @ApiModelProperty(value = "成熟度过程值")
    private java.lang.String maturitygcz;
	/**外置成熟度过程值*/
	@Excel(name = "外置成熟度过程值", width = 15)
    @ApiModelProperty(value = "外置成熟度过程值")
    private java.lang.String maturityoutsidegcz;
	/**强度过程值*/
	@Excel(name = "强度过程值", width = 15)
    @ApiModelProperty(value = "强度过程值")
    private java.lang.String strengthgcz;
	/**外置强度过程值*/
	@Excel(name = "外置强度过程值", width = 15)
    @ApiModelProperty(value = "外置强度过程值")
    private java.lang.String longtextoutsidegcz;
	/**温度过程值*/
	@Excel(name = "温度过程值", width = 15)
    @ApiModelProperty(value = "温度过程值")
    private java.lang.String tempgcz;
	/**外置温度过程值*/
	@Excel(name = "外置温度过程值", width = 15)
    @ApiModelProperty(value = "外置温度过程值")
    private java.lang.String tempoutsidegcz;
}
