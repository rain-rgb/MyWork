package com.trtm.iot.largesbdoorswitch.entity;

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
 * @Description: largesb_door_switch
 * @Author: jeecg-boot
 * @Date:   2023-12-26
 * @Version: V1.0
 */
@Data
@TableName("largesb_door_switch")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="largesb_door_switch对象", description="largesb_door_switch")
public class LargesbDoorSwitch implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id（大型设备门开关数据）*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id（大型设备门开关数据）")
    private java.lang.Integer id;
	/**大型设备门开关组织机构*/
    @ApiModelProperty(value = "大型设备门开关组织机构")
    private java.lang.String sysOrgCode;
	/**大型设备门开关编号*/
	@Excel(name = "大型设备门开关编号", width = 15)
    @ApiModelProperty(value = "大型设备门开关编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String sbjNo;
	/**门状态（0：进入，1：离开）*/
	@Excel(name = "门状态（0：进入，1：离开）", width = 15)
    @ApiModelProperty(value = "门状态（0：进入，1：离开）")
    private java.lang.Integer doorStatus;
	/**开门时间*/
	@Excel(name = "开门时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开门时间")
    private java.util.Date openTime;
	/**开门人员*/
	@Excel(name = "开门人员", width = 15)
    @ApiModelProperty(value = "开门人员")
    private java.lang.String openBy;
	/**工作时长（单位：分）*/
	@Excel(name = "工作时长（单位：分）", width = 15)
    @ApiModelProperty(value = "工作时长（单位：分）")
    private java.lang.String workTime;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String sbjLongitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String sbjLatitude;
}
