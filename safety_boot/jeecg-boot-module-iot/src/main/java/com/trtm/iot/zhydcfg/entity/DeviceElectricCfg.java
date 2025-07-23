package com.trtm.iot.zhydcfg.entity;

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
 * @Description: 智慧用电配置主表
 * @Author: jeecg-boot
 * @Date:   2021-05-18
 * @Version: V1.0
 */
@Data
@TableName("device_electric_cfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_electric_cfg对象", description="智慧用电配置主表")
public class DeviceElectricCfg implements Serializable {
    private static final long serialVersionUID = 1L;
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**主键uuid*/
    @ApiModelProperty(value = "主键uuid")
    private java.lang.String uid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String imei;
    /**是否报警:0=报警,1=不报警*/
    @Excel(name = "是否报警:0=报警,1=不报警", width = 15)
    @ApiModelProperty(value = "是否报警:0=报警,1=不报警")
    @Dict(dicCode = "is_call")
    private java.lang.Integer isCall;
	/**电压A标准值*/
	@Excel(name = "电压A标准值", width = 15)
    @ApiModelProperty(value = "电压A标准值")
    private java.lang.Double voltagebza;
	/**电压B标准值*/
	@Excel(name = "电压B标准值", width = 15)
    @ApiModelProperty(value = "电压B标准值")
    private java.lang.Double voltagebzb;
	/**电压C标准值*/
	@Excel(name = "电压C标准值", width = 15)
    @ApiModelProperty(value = "电压C标准值")
    private java.lang.Double voltagebzc;
	/**频率A标准值*/
	@Excel(name = "频率A标准值", width = 15)
    @ApiModelProperty(value = "频率A标准值")
    private java.lang.Double frequencybza;
	/**频率B标准值*/
	@Excel(name = "频率B标准值", width = 15)
    @ApiModelProperty(value = "频率B标准值")
    private java.lang.Double frequencybzb;
	/**频率C标准值*/
	@Excel(name = "频率C标准值", width = 15)
    @ApiModelProperty(value = "频率C标准值")
    private java.lang.Double frequencybzc;
	/**电流A标准值*/
	@Excel(name = "电流A标准值", width = 15)
    @ApiModelProperty(value = "电流A标准值")
    private java.lang.Double electricitybza;
	/**电流B标准值*/
	@Excel(name = "电流B标准值", width = 15)
    @ApiModelProperty(value = "电流B标准值")
    private java.lang.Double electricitybzb;
	/**电流C标准值*/
	@Excel(name = "电流C标准值", width = 15)
    @ApiModelProperty(value = "电流C标准值")
    private java.lang.Double electricitybzc;
	/**漏电流标准值*/
	@Excel(name = "漏电流标准值", width = 15)
    @ApiModelProperty(value = "漏电流标准值")
    private java.lang.Double seepelectricitybz;
	/**温度A标准值*/
	@Excel(name = "温度A标准值", width = 15)
    @ApiModelProperty(value = "温度A标准值")
    private java.lang.Double tempbza;
	/**温度B标准值*/
	@Excel(name = "温度B标准值", width = 15)
    @ApiModelProperty(value = "温度B标准值")
    private java.lang.Double tempbzb;
	/**温度C标准值*/
	@Excel(name = "温度C标准值", width = 15)
    @ApiModelProperty(value = "温度C标准值")
    private java.lang.Double tempbzc;
	/**温度N标准值*/
	@Excel(name = "温度N标准值", width = 15)
    @ApiModelProperty(value = "温度N标准值")
    private java.lang.Double tempbzn;
	/**电能A标准值*/
	@Excel(name = "电能A标准值", width = 15)
    @ApiModelProperty(value = "电能A标准值")
    private java.lang.Double energybza;
	/**电能B标准值*/
	@Excel(name = "电能B标准值", width = 15)
    @ApiModelProperty(value = "电能B标准值")
    private java.lang.Double energybzb;
	/**电能C标准值*/
	@Excel(name = "电能C标准值", width = 15)
    @ApiModelProperty(value = "电能C标准值")
    private java.lang.Double energybzc;
    /**是否删除*/
    @Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.Integer isdel;
    /**智慧用电超标号码*/
    @Excel(name = "智慧用电超标号码", width = 15)
    @ApiModelProperty(value = "智慧用电超标号码")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String zhydPhones;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**更新时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**权限配置*/
    @ApiModelProperty(value = "权限配置")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
}
