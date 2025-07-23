package com.trtm.iot.suchingcarpeiz.entity;

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
 * @Description: suching_carpeiz
 * @Author: jeecg-boot
 * @Date:   2023-11-10
 * @Version: V1.0
 */
@Data
@TableName("suching_carpeiz")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="suching_carpeiz对象", description="suching_carpeiz")
public class SuchingCarpeiz implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**主键uuid*/
	@Excel(name = "主键uuid", width = 15)
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
    private java.lang.Integer isCall;
	/**发车单手机号码*/
	@Excel(name = "发车单手机号码", width = 15)
    @ApiModelProperty(value = "发车单手机号码")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String zhydPhones;
	/**备注(预计时间)*/
	@Excel(name = "备注(预计时间)", width = 15)
    @ApiModelProperty(value = "备注(预计时间)")
    private java.lang.String remark;
	/**时长单位 0 分钟 1 小时 2 天*/
	@Excel(name = "时长单位 0 分钟 1 小时 2 天", width = 15)
    @ApiModelProperty(value = "时长单位 0 分钟 1 小时 2 天")
    @Dict(dicCode = "unit")
    private java.lang.Integer unit;
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
}
