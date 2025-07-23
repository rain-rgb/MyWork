package com.trtm.iot.ckqdatahandle.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("device_mixin_station_details")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DeviceMixinStationDetails", description = "搅拌站详情")
public class DeviceMixinStationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeviceMixinStationDetails() {
    };

    public DeviceMixinStationDetails(String bidCode, String mixinStationCode, Date uploadTime) {
        this.bidCode = bidCode;
        this.mixinStationCode = mixinStationCode;
        this.uploadTime = uploadTime;
        this.sid = "/usr/plcnet/BP" + bidCode + "_" + mixinStationCode + "_STA/edge/u";
        this.isDelete = "1";
    }

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    @ApiModelProperty(value = "搅拌站名称")
    private String mixinStationCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出料时间")
    private Date dischargeTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    @ApiModelProperty(value = "当日-水泥")
    private Double todayCement;

    @ApiModelProperty(value = "当日-粉煤灰")
    private Double todayFlyAsh;

    @ApiModelProperty(value = "当日-材料用量")
    private Double todayCementFlyAsh;

    @ApiModelProperty(value = "累计-水泥")
    private Double totalCement;

    @ApiModelProperty(value = "累计-粉煤灰")
    private Double totalFlyAsh;

    @ApiModelProperty(value = "累计-材料用量")
    private Double totalCementFlyAsh;

    @ApiModelProperty("总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "理论-水泥")
    private Double theoryCement;

    @ApiModelProperty(value = "实际-水泥")
    private Double realityCement;

    @ApiModelProperty(value = "水泥-误差率")
    private Double cementErrorRate;

    @ApiModelProperty(value = "理论-粉煤灰")
    private Double theoryFlyAsh;

    @ApiModelProperty(value = "实际-粉煤灰")
    private Double realityFlyAsh;

    @ApiModelProperty(value = "粉煤灰-误差率")
    private Double flyAshErrorRate;

    @ApiModelProperty(value = "比例值（用于区分不同的理论值）")
    private Double proportionalValue;

    @ApiModelProperty(value = "理论-水泥-1")
    private Double theoryCementOne;

    @ApiModelProperty(value = "实际-水泥-1")
    private Double realityCementOne;

    @ApiModelProperty(value = "水泥-1-误差率")
    private Double cementOneErrorRate;

    @ApiModelProperty(value = "理论-水泥-2")
    private Double theoryCementTwo;

    @ApiModelProperty(value = "实际-水泥-2")
    private Double realityCementTwo;

    @ApiModelProperty(value = "水泥-2-误差率")
    private Double cementTwoErrorRate;

    @ApiModelProperty(value = "理论-粉煤灰-1")
    private Double theoryFlyAshOne;

    @ApiModelProperty(value = "实际-粉煤灰-1")
    private Double realityFlyAshOne;

    @ApiModelProperty(value = "粉煤灰-1-误差率")
    private Double flyAshOneErrorRate;

    @ApiModelProperty(value = "理论-粉煤灰-2")
    private Double theoryFlyAshTwo;

    @ApiModelProperty(value = "实际-粉煤灰-2")
    private Double realityFlyAshTwo;

    @ApiModelProperty(value = "粉煤灰-2-误差率")
    private Double flyAshTwoErrorRate;

    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sid;

    @ApiModelProperty(value = "数据展示状态（0-统计数据（理论实际和）1-不做展示（基础数据）")
    private String isDelete;


}
