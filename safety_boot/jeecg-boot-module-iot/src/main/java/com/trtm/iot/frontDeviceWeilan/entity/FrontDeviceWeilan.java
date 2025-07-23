package com.trtm.iot.frontDeviceWeilan.entity;

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
 * @Description: 运输车电子围栏数据
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
@Data
@TableName("front_device_weilan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="front_device_weilan对象", description="运输车电子围栏数据")
public class FrontDeviceWeilan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeiNo;
	/**sid为猎鹰service唯一编号*/
	@Excel(name = "sid为猎鹰service唯一编号", width = 15)
    @ApiModelProperty(value = "sid为猎鹰service唯一编号")
    private java.lang.String sid;
	/**围栏名称*/
	@Excel(name = "围栏名称", width = 15)
    @ApiModelProperty(value = "围栏名称")
    private java.lang.String name;
	/**围栏描述*/
	@Excel(name = "围栏描述", width = 15)
    @ApiModelProperty(value = "围栏描述")
    private java.lang.String descs;
	/**多边形顶点坐标*/
	@Excel(name = "多边形顶点坐标", width = 15)
    @ApiModelProperty(value = "多边形顶点坐标")
    private java.lang.String points;
	/**中心点坐标(圆形)*/
	@Excel(name = "中心点坐标(圆形)", width = 15)
    @ApiModelProperty(value = "中心点坐标(圆形)")
    private java.lang.String center;
	/**由系统自动生成，按正整数递增*/
	@Excel(name = "由系统自动生成，按正整数递增", width = 15)
    @ApiModelProperty(value = "由系统自动生成，按正整数递增")
    private java.lang.Integer gfid;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**米 整数，取值范[1,50000]*/
	@Excel(name = "米 整数，取值范[1,50000]", width = 15)
    @ApiModelProperty(value = "米 整数，取值范[1,50000]")
    private java.lang.Integer radius;
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	private java.lang.Integer status;
    private java.lang.Double lng;
    private java.lang.Double lat;
    private java.lang.Integer tid;//终端 id
    private java.lang.String tname;//终端 name
    private java.lang.Integer isdel;//0 未删除  1已删除
    private java.lang.Integer delstatus;//0 未删除  1已删除

    // 二级围栏
    private java.lang.Integer radius2;
}
