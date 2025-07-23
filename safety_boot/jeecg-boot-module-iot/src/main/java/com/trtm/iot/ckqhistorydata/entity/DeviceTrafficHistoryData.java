package com.trtm.iot.ckqhistorydata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("device_traffic_historydata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DeviceTrafficHistoryData对象", description = "采空区历史表信息")
public class DeviceTrafficHistoryData {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号（弃用）")
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


    @Excel(name = "对应的值", width = 15)
    @ApiModelProperty(value = "对应的值")
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

    @ApiModelProperty("数据处理状态(0-未处理，1-已处理)")
    private String handleStatus;

    @ApiModelProperty("数据处理状态(0-未处理，1-已处理)")
    private String handleStatusTwo;

    @ApiModelProperty("数据处理状态(0-未处理，1-已处理)")
    private String handleStatusThree;


}
