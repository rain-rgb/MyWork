package com.trtm.iot.vehicleinfraredtemperature.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: vehicle_infrared_temperature
 * @Author: jeecg-boot
 * @Date: 2023-08-11
 * @Version: V1.0
 */
@Data
@TableName("vehicle_infrared_temperature")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vehicle_infrared_temperature对象", description = "vehicle_infrared_temperature")
public class VehicleInfraredTemperature implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String deviceId;
    /**
     * 采集时间
     */
    @Excel(name = "采集时间", width = 15)
    @ApiModelProperty(value = "采集时间")
    private String collectionTime;
    /**
     * 温度
     */
    @Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private Double t;
    /**
     * 网络传输类型：WIFI、4G
     */
    @Excel(name = "网络传输类型：WIFI、4G", width = 15)
    @ApiModelProperty(value = "网络传输类型：WIFI、4G")
    private String type;
}
