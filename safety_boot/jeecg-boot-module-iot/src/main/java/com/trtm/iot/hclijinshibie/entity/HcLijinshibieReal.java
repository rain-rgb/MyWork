package com.trtm.iot.hclijinshibie.entity;

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
 * @Description: hc_lijinshibie_real
 * @Author: jeecg-boot
 * @Date:   2024-11-25
 * @Version: V1.0
 */
@Data
@TableName("hc_lijinshibie_real")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_lijinshibie_real对象", description="hc_lijinshibie_real")
public class HcLijinshibieReal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**位置*/
	@Excel(name = "位置", width = 15)
    @ApiModelProperty(value = "位置")
    private java.lang.String weizhi;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String jingdu;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String weidu;
	/**识别图片*/
	@Excel(name = "识别图片", width = 15)
    @ApiModelProperty(value = "识别图片")
    private java.lang.String shibiepic;
	/**问题描述*/
	@Excel(name = "问题描述", width = 15)
    @ApiModelProperty(value = "问题描述")
    private java.lang.String problem;
	/**超标等级: 0正常，1异常*/
	@Excel(name = "超标等级: 0正常，1异常", width = 15)
    @ApiModelProperty(value = "超标等级: 0正常，1异常")
    private java.lang.Integer overLevel;
	/**控制权限*/
    @ApiModelProperty(value = "控制权限")
    private java.lang.String sysOrgCode;
	/**审核状态20为已闭合*/
	@Excel(name = "审核状态20为已闭合", width = 15)
    @ApiModelProperty(value = "审核状态20为已闭合")
    private java.lang.Integer overproofStatus;
	/**0 停止；1 运行；2gps故障*/
	@Excel(name = "0 停止；1 运行；2gps故障", width = 15)
    @ApiModelProperty(value = "0 停止；1 运行；2gps故障")
    private java.lang.Integer cheliangzhuangtai;
	/**车辆运行速度*/
	@Excel(name = "车辆运行速度", width = 15)
    @ApiModelProperty(value = "车辆运行速度")
    private java.lang.String sudu;
	/**gps设备编号*/
	@Excel(name = "gps设备编号", width = 15)
    @ApiModelProperty(value = "gps设备编号")
    private java.lang.String shebeino;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**摄像头id*/
	@Excel(name = "摄像头id", width = 15)
    @ApiModelProperty(value = "摄像头id")
    private java.lang.String shexiangtouid;
}
