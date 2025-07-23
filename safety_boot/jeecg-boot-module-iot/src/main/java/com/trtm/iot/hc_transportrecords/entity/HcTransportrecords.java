package com.trtm.iot.hc_transportrecords.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 运输数据
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
@ApiModel(value="hc_transportrecords对象", description="运输数据")
@Data
@TableName("hc_transportrecords")
public class HcTransportrecords implements Serializable {
    private static final long serialVersionUID = 1L;

	/**数据id，唯一递增*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "数据id，唯一递增")
    private java.lang.Integer id;
	/**运输车id*/
	@Excel(name = "运输车id", width = 15)
    @ApiModelProperty(value = "运输车id")
    @Dict(dictTable = "hc_truck", dicText = "licensePlate", dicCode = "truckId")
    private java.lang.String truckid;
	/**拌和站id*/
	@Excel(name = "拌和站id", width = 15)
    @ApiModelProperty(value = "拌和站id")
    private java.lang.String stationid;
	/**拌和站名称*/
	@Excel(name = "拌和站名称", width = 15)
    @ApiModelProperty(value = "拌和站名称")
    private java.lang.String stationname;
	/**出厂时间*/
	@Excel(name = "出厂时间", width = 15, format = "yyyy-MM-dd hh:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "出厂时间")
    private java.util.Date outstationtime;
	/**运输时长*/
	@Excel(name = "运输时长", width = 15)
    @ApiModelProperty(value = "运输时长")
    private java.lang.String transporttime;
    private java.lang.String temperature;
    private java.lang.String projectid;
    private java.lang.String sectionid;
    private java.lang.Integer isdj;
}
