/**
 * @ClassName DeviceMixinStationUpdateLog.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年08月02日 14:59:00
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
@TableName("device_mixin_station_update_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DeviceMixinStationUpdateLog", description = "搅拌站信息修改记录")
public class DeviceMixinStationUpdateLog {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    @ApiModelProperty(value = "搅拌站号")
    private String mixinStationCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "配料总量修改值")
    private Double batchingProductionUpdate;

    @ApiModelProperty(value = "注浆总量修改值")
    private Double groutingTotalUpdate;

    @ApiModelProperty(value = "数据处理状态（0-未处理，1-已处理）")
    private Double handleStatus;

    @ApiModelProperty(value = "数据处理成功状态（0-成功，1-失败）")
    private Double updateStatus;



    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sid;

    @ApiModelProperty(value = "删除状态（0-未删除，1-已删除）")
    private String isDelete;


}


