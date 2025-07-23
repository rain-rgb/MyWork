package com.trtm.iot.car.entity;

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
 * @Description: car_dispatch
 * @Author: jeecg-boot
 * @Date:   2021-11-30
 * @Version: V1.0
 */
@Data
@TableName("car_dispatch")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="car_dispatch对象", description="car_dispatch")
public class CarDispatch implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String uuid;
	/**起点*/
	@Excel(name = "起点", width = 15)
    @ApiModelProperty(value = "起点")
    private java.lang.String startingPoint;
	/**终点*/
	@Excel(name = "终点", width = 15)
    @ApiModelProperty(value = "终点")
    private java.lang.String end;
	/**里程(公里)*/
	@Excel(name = "里程(公里)", width = 15)
    @ApiModelProperty(value = "里程(公里)")
    private java.lang.Double mileage;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:ss:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:ss:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**状态 1未完成  2已完成*/
	@Excel(name = "状态 1未完成  2已完成", width = 15)
    @ApiModelProperty(value = "状态 1未完成  2已完成")
    @Dict(dicCode = "carstatus")
    private java.lang.Integer status;
	/**所属组织机构*/
    @ApiModelProperty(value = "所属组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**指派人*/
	@Excel(name = "指派人", width = 15)
    @ApiModelProperty(value = "指派人")
    private java.lang.String designator;
    /**车牌*/
    @Excel(name = "车牌", width = 15)
    @ApiModelProperty(value = "车牌")
    private java.lang.String licenseplate;
    /**执行开始时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")
    @ApiModelProperty(value = "执行开始时间")
    private java.util.Date zxstarttime;
    /**执行结束时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")
    @ApiModelProperty(value = "执行结束时间")
    private java.util.Date zxendtime;
    @ApiModelProperty(value = "手机号")
    private java.lang.String iphones;
}
