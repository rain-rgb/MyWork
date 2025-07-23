/**
 * @ClassName DeviceGroutingPumpDay.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月09日 09:10:00
 */
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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("device_grouting_pump_day")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DeviceGroutingPumpDay", description = "注浆泵日注浆量信息")
public class DeviceGroutingPumpDay {

    public DeviceGroutingPumpDay(String bidCode, String groutingPumpName, String groutingPumpCode, Date Time) {
        this.bidCode = bidCode;
        this.groutingPumpName = groutingPumpName;
        this.groutingPumpCode = groutingPumpCode;
        this.gpNameCode = groutingPumpName + "号站-" + groutingPumpCode + "号注浆泵";
        this.Time = Time;
        this.sid = "/usr/plcnet/BP" + bidCode + "_" + groutingPumpName + "_PUMP/edge/u";
    }

    public DeviceGroutingPumpDay() {
    }

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

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private Date Time;

    @ApiModelProperty(value = "注浆总量")
    private Double groutingTotal;

    @ApiModelProperty(value = "当日注浆总量")
    private Double todayGroutingTotal;

    @ApiModelProperty(value = "累计（所有天数加起来）")
    private Double allDayGroutingTotal;

    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sid;

    @ApiModelProperty(value = "注浆泵")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String zjb;

    @ApiModelProperty(value = "删除状态（0-未删除，1-已删除）")
    private String isDelete;


}


