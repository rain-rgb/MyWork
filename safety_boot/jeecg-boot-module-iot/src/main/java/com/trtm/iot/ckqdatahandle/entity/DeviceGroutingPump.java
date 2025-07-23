package com.trtm.iot.ckqdatahandle.entity;


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

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("device_grouting_pump")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DeviceGroutingPump", description = "注浆泵信息")
public class DeviceGroutingPump {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    @ApiModelProperty(value = "注浆泵名称")
    private String groutingPumpName;

    @ApiModelProperty(value = "注浆泵编号")
    private String groutingPumpCode;

    @ApiModelProperty(value = "注浆泵名称+注浆泵编号")
    private String gpNameCode;

    @ApiModelProperty(value = "施工井号")
    private String constructionWellCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "注浆开始时间")
    private Date groutingStartTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "注浆结束时间")
    private Date groutingEndTime;

    @ApiModelProperty(value = "累计注浆量")
    private Double groutingTotal;

    @ApiModelProperty(value = "注浆压力")
    private BigDecimal groutingPressure;

    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sid;


    @ApiModelProperty(value = "数据完成状态（0-未完成，1-已完成）")
    private String completeStatus;


    @ApiModelProperty(value = "删除状态（0-未删除，1-已删除）")
    private String isDelete;
}
