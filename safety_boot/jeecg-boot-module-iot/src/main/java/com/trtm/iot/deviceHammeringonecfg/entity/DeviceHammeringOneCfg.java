package com.trtm.iot.deviceHammeringonecfg.entity;

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
 * @Description: device_hammering_one_cfg
 * @Author: jeecg-boot
 * @Date:   2024-03-13
 * @Version: V1.0
 */
@Data
@TableName("device_hammering_one_cfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_hammering_one_cfg对象", description="device_hammering_one_cfg")
public class DeviceHammeringOneCfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**设计桩长(m)*/
	@Excel(name = "设计桩长(m)", width = 15)
    @ApiModelProperty(value = "设计桩长(m)")
    private java.lang.String designdep;
	/**锤击次数*/
	@Excel(name = "锤击次数", width = 15)
    @ApiModelProperty(value = "锤击次数")
    private java.lang.Integer pileSecond;
	/**倾角下限*/
	@Excel(name = "倾角下限", width = 15)
    @ApiModelProperty(value = "倾角下限")
    private java.lang.String xx;
	/**倾角上限*/
	@Excel(name = "倾角上限", width = 15)
    @ApiModelProperty(value = "倾角上限")
    private java.lang.String xs;
	/**软基预警号码*/
	@Excel(name = "软基预警号码", width = 15)
    @ApiModelProperty(value = "软基预警号码")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String jbzphones;
	/**是否报警:0=报警,1=不报警*/
	@Excel(name = "是否报警:0=报警,1=不报警", width = 15)
    @Dict(dicCode = "is_call")
    private java.lang.Integer isCall;
	/**权限配置*/
    @ApiModelProperty(value = "权限配置")
    private java.lang.String sysOrgCode;
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
	/**是否删除（0：未删除，1：已删除）*/
	@Excel(name = "是否删除（0：未删除，1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private java.lang.Integer isdel;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
}
