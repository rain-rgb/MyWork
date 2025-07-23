package com.trtm.iot.shebeiWarncfg.entity;

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
 * @Description: 设备状态预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-08-04
 * @Version: V1.0
 */
@Data
@TableName("shebei_warncfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="shebei_warncfg对象", description="设备状态预警配置表")
public class ShebeiWarncfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**自增id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增id")
    private java.lang.Integer id;
	/**主键uuid*/
	@Excel(name = "主键uuid", width = 15)
    @ApiModelProperty(value = "主键uuid")
    private java.lang.String uid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**是否报警:0=报警,1=不报警*/
	@Excel(name = "是否报警:0=报警,1=不报警", width = 15)
    @ApiModelProperty(value = "是否报警:0=报警,1=不报警")
    @Dict(dicCode = "is_call")
    private java.lang.Integer isCall;
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
    private java.lang.String createBy;
	/**设备状态预警号码组*/
	@Excel(name = "设备状态预警号码组", width = 15)
    @ApiModelProperty(value = "设备状态预警号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String phone;
    /**设备类型*/
    @Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    @Dict(dicCode = "sbtype")
    private java.lang.Integer sbtype;
}
