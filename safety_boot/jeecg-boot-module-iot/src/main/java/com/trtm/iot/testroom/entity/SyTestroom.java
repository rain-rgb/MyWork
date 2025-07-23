package com.trtm.iot.testroom.entity;

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
 * @Description: sy_testroom
 * @Author: jeecg-boot
 * @Date:   2024-09-09
 * @Version: V1.0
 */
@Data
@TableName("sy_testroom")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_testroom对象", description="sy_testroom")
public class SyTestroom implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**试验室*/
	@Excel(name = "试验室", width = 15)
    @ApiModelProperty(value = "试验室")
    private java.lang.String testroom;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private java.lang.String roomType;
	/**面积*/
	@Excel(name = "面积", width = 15)
    @ApiModelProperty(value = "面积")
    private java.lang.String roomArea;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    @ApiModelProperty(value = "负责人")
    private java.lang.String fuzer;
	/**设备数*/
	@Excel(name = "设备数", width = 15)
    @ApiModelProperty(value = "设备数")
    private java.lang.String shebeishu;
	/**人员数*/
	@Excel(name = "人员数", width = 15)
    @ApiModelProperty(value = "人员数")
    private java.lang.String renyuanshu;
	/**环境设备*/
	@Excel(name = "环境设备", width = 15)
    @ApiModelProperty(value = "环境设备")
    private java.lang.String huanjsb;
	/**温度要求*/
	@Excel(name = "温度要求", width = 15)
    @ApiModelProperty(value = "温度要求")
    private java.lang.String wendu1;
	/**实时温度*/
	@Excel(name = "实时温度", width = 15)
    @ApiModelProperty(value = "实时温度")
    private java.lang.String wendu2;
	/**湿度要求*/
	@Excel(name = "湿度要求", width = 15)
    @ApiModelProperty(value = "湿度要求")
    private java.lang.String shidu1;
	/**实时湿度*/
	@Excel(name = "实时湿度", width = 15)
    @ApiModelProperty(value = "实时湿度")
    private java.lang.String shidu2;
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
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**设备列表*/
	@Excel(name = "设备列表", width = 15)
    @ApiModelProperty(value = "设备列表")
    private java.lang.String shebeis;
	/**人员列表*/
	@Excel(name = "人员列表", width = 15)
    @ApiModelProperty(value = "人员列表")
    private java.lang.String renyuans;

    // 云检检测室id
    private java.lang.String yunjianid;
}
