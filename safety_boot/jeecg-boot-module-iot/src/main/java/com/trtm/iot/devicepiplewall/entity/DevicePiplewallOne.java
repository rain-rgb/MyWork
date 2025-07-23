package com.trtm.iot.devicepiplewall.entity;

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
 * @Description: device_piplewall_one
 * @Author: jeecg-boot
 * @Date:   2024-12-28
 * @Version: V1.0
 */
@Data
@TableName("device_piplewall_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_piplewall_one对象", description="device_piplewall_one")
public class DevicePiplewallOne implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.String pileNo;
	/**施工深度(m)*/
	@Excel(name = "施工深度(m)", width = 15)
    @ApiModelProperty(value = "施工深度(m)")
    private java.lang.String pileRealdep;
	/**工作时长(min)*/
	@Excel(name = "工作时长(min)", width = 15)
    @ApiModelProperty(value = "工作时长(min)")
    private java.lang.String pileWorktime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15)
    @ApiModelProperty(value = "结束时间")
    private java.lang.String pileEndtime;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private java.lang.String pileStarttime;
	/**倾斜度*/
	@Excel(name = "倾斜度", width = 15)
    @ApiModelProperty(value = "倾斜度")
    private java.lang.String pileY;
	/**下钻速度(cm/min)*/
	@Excel(name = "下钻速度(cm/min)", width = 15)
    @ApiModelProperty(value = "下钻速度(cm/min)")
    private java.lang.String downSpeed;
	/**提钻速度(cm/min)*/
	@Excel(name = "提钻速度(cm/min)", width = 15)
    @ApiModelProperty(value = "提钻速度(cm/min)")
    private java.lang.Double upSpeed;
	/**设计桩长*/
	@Excel(name = "设计桩长", width = 15)
    @ApiModelProperty(value = "设计桩长")
    private java.lang.Integer pileDesigndep;
	/**关联软基任务单 device_mixpile_rwd*/
	@Excel(name = "关联软基任务单 device_mixpile_rwd", width = 15)
    @ApiModelProperty(value = "关联软基任务单 device_mixpile_rwd")
    private java.lang.String rjrwd;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String pileMileage;
	/**桩经度*/
	@Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private java.lang.String pileLgd;
	/**桩纬度*/
	@Excel(name = "桩纬度", width = 15)
    @ApiModelProperty(value = "桩纬度")
    private java.lang.String pileLtd;
	/**超标等级0：合格；1不合格*/
	@Excel(name = "超标等级0：合格；1不合格", width = 15)
    @ApiModelProperty(value = "超标等级0：合格；1不合格")
    private java.lang.Integer chaobiaodengji;
	/**0未处置；10：处置；20：闭合，30驳回*/
	@Excel(name = "0未处置；10：处置；20：闭合，30驳回", width = 15)
    @ApiModelProperty(value = "0未处置；10：处置；20：闭合，30驳回")
    private java.lang.Integer overproofStatus;
	/**异常原因*/
	@Excel(name = "异常原因", width = 15)
    @ApiModelProperty(value = "异常原因")
    private java.lang.String ycyy;
	/**接桩次数*/
	@Excel(name = "接桩次数", width = 15)
    @ApiModelProperty(value = "接桩次数")
    private java.lang.Integer jiegantimes;
	/**接杆长度*/
	@Excel(name = "接杆长度", width = 15)
    @ApiModelProperty(value = "接杆长度")
    private java.lang.String jieganchangdu;
	/**长度(cm)*/
	@Excel(name = "长度(cm)", width = 15)
    @ApiModelProperty(value = "长度(cm)")
    private java.lang.String changdu;
	/**宽度(cm)*/
	@Excel(name = "宽度(cm)", width = 15)
    @ApiModelProperty(value = "宽度(cm)")
    private java.lang.String kuandu;
	/**总方量m³*/
	@Excel(name = "总方量m³", width = 15)
    @ApiModelProperty(value = "总方量m³")
    private java.lang.String zongm;
	/**电流*/
	@Excel(name = "电流", width = 15)
    @ApiModelProperty(value = "电流")
    private java.lang.String dianliu;
	/**泥浆密度*/
	@Excel(name = "泥浆密度", width = 15)
    @ApiModelProperty(value = "泥浆密度")
    private java.lang.String pileDensity;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String uuid;
	/**倾斜度x*/
	@Excel(name = "倾斜度x", width = 15)
    @ApiModelProperty(value = "倾斜度x")
    private java.lang.String pileX;
}
