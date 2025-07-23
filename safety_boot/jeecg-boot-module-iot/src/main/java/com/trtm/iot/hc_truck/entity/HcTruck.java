package com.trtm.iot.hc_truck.entity;

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
 * @Description: 获取运输车信息
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Data
@TableName("hc_truck")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_truck对象", description="获取运输车信息")
public class HcTruck implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**记录id*/
	@Excel(name = "记录id", width = 15)
    @ApiModelProperty(value = "记录id")
    private java.lang.Integer jlid;
	/**运输车id*/
	@Excel(name = " 运输车id", width = 15)
    @ApiModelProperty(value = " 运输车id")
    private java.lang.String truckid;
	/**MMIT编号*/
	@Excel(name = "MMIT编号", width = 15)
    @ApiModelProperty(value = "MMIT编号")
    private java.lang.String mmit;
	/**RFID号*/
	@Excel(name = "RFID号", width = 15)
    @ApiModelProperty(value = "RFID号")
    private java.lang.String rfid;
	/**车牌号*/
	@Excel(name = "车牌号", width = 15)
    @ApiModelProperty(value = "车牌号")
    private java.lang.String licenseplate;
	/**驾驶员*/
	@Excel(name = "驾驶员", width = 15)
    @ApiModelProperty(value = "驾驶员")
    private java.lang.String drivername;
	/**驾驶员联系方式*/
	@Excel(name = "驾驶员联系方式", width = 15)
    @ApiModelProperty(value = "驾驶员联系方式")
    private java.lang.String driverphone;
	/**运输车类型，51-混合料运输车,52-沥青运输车*/
	@Excel(name = "运输车类型，51-混合料运输车,52-沥青运输车", width = 15)
    @ApiModelProperty(value = "运输车类型，51-混合料运输车,52-沥青运输车")
    private java.lang.Integer trucktype;
	private java.lang.String pjid;
	private java.lang.String sectionid;
	private java.lang.String orgcode;
}
