package com.trtm.iot.hc_transportrecords.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.hc_truck.entity.HcTruck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 运输数据
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
@ApiModel(value="hc_transportrecords对象", description="运输数据")
@Data
@TableName("hc_transportrecords")
public class HcTransportrecordsYSC implements Serializable {
    private static final long serialVersionUID = 1L;

	/**数据id，唯一递增*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "数据id，唯一递增")
    private Integer id;
	/**运输车id*/
	@Excel(name = "运输车id", width = 15)
    @ApiModelProperty(value = "运输车id")
    @Dict(dictTable = "hc_truck", dicText = "licensePlate", dicCode = "truckId")
    private String truckid;
	/**拌和站id*/
	@Excel(name = "拌和站id", width = 15)
    @ApiModelProperty(value = "拌和站id")
    private String stationid;
	/**拌和站名称*/
	@Excel(name = "拌和站名称", width = 15)
    @ApiModelProperty(value = "拌和站名称")
    private String stationname;
	/**出厂时间*/
	@Excel(name = "出厂时间", width = 15, format = "yyyy-MM-dd hh:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "出厂时间")
    private Date outstationtime;
	/**运输时长*/
	@Excel(name = "运输时长", width = 15)
    @ApiModelProperty(value = "运输时长")
    private String transporttime;
    private String temperature;
    private String projectid;
    private String sectionid;
    private Integer isdj;

    private HcTruck hcTruck;
}
