package com.trtm.iot.devicecranecfg.entity;

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
 * @Description: 大型设备(提梁机/架桥机)配置数据表
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Data
@TableName("device_crane_cfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_crane_cfg对象", description="大型设备(提梁机/架桥机)配置数据表")
public class DeviceCraneCfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String deviceCode;
	/**主键uuid*/
	@Excel(name = "主键uuid", width = 15)
    @ApiModelProperty(value = "主键uuid")
    private java.lang.String uid;
	/**主钩吊重超额重比例(%)*/
	@Excel(name = "主钩吊重超额重比例(%)", width = 15)
    @ApiModelProperty(value = "主钩吊重超额重比例(%)")
    private java.lang.Double mainHookloadlv;
	/**副钩吊重超额重比例(%)*/
	@Excel(name = "副钩吊重超额重比例(%)", width = 15)
    @ApiModelProperty(value = "副钩吊重超额重比例(%)")
    private java.lang.Double vicehookloadlv;
	/**风速报警值*/
	@Excel(name = "风速报警值", width = 15)
    @ApiModelProperty(value = "风速报警值")
    private java.lang.Double windSpeedalarm;
	/**激光测距偏差值*/
	@Excel(name = "激光测距偏差值", width = 15)
    @ApiModelProperty(value = "激光测距偏差值")
    private java.lang.Double slr;
	/**是否删除（0：未删除，1：已删除）*/
	@Excel(name = "是否删除（0：未删除，1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private java.lang.Integer isdel;
	/**是否报警:0=报警,1=不报警*/
	@Excel(name = "是否报警:0=报警,1=不报警", width = 15)
    @ApiModelProperty(value = "是否报警:0=报警,1=不报警")
    @Dict(dicCode = "is_call")
    private java.lang.Integer isCall;
	/**提梁机手机号码组*/
	@Excel(name = "提梁机手机号码组", width = 15)
    @ApiModelProperty(value = "提梁机手机号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String cranePhones;
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
    /**副钩吊重超主钩吊重比例(%)*/
    @Excel(name = "副钩吊重超主钩吊重比例(%)", width = 15)
    @ApiModelProperty(value = "副钩吊重超主钩吊重比例(%)")
    private java.lang.Double mainVicelv;
}
