package com.trtm.iot.ckqrealdata.entity;

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
 * @Description: 采空区实时表信息
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
@Data
@TableName("device_traffic_realdata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "device_traffic_realdata对象", description = "采空区实时表信息")
public class DeviceTrafficRealdata implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号(弃用)")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sid;

    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "主题")
    private String topic;

    @Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "数据上传时间")
    private Date datatime;

    @Excel(name = "设备信息", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dicCode = "pidreal")
    private String pid;

    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String v;

    @Excel(name = "s", width = 15)
    @ApiModelProperty(value = "s")
    private Integer s;

    @Excel(name = "ms", width = 15)
    @ApiModelProperty(value = "ms")
    private Integer ms;


    @Excel(name = "e", width = 15)
    @ApiModelProperty(value = "e")
    private Integer e;
}
