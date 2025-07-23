package com.trtm.iot.ckqdatahandle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

@Data
@TableName("device_mixin_station")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DeviceMixinStation", description = "搅拌站信息")
public class DeviceMixinStation implements Serializable {
    private static final long serialVersionUID = 1L;

    public DeviceMixinStation() {
    }
    public DeviceMixinStation(String bidCode, String mixinStationCode) {
        this.bidCode = bidCode;
        this.mixinStationCode = mixinStationCode;
        this.sid = "/usr/plcnet/BP" + bidCode + "_" + mixinStationCode + "_STA/edge/u";
    }

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "标段号")
    private String bidCode;

    @ApiModelProperty(value = "搅拌站号")
    private String mixinStationCode;

    @ApiModelProperty(value = "配料生产总量")
    private Double batchingProductionTotal;

    @ApiModelProperty(value = "已注浆井数量")
    private Integer groutingWellsNumber;

    @ApiModelProperty(value = "注浆总量")
    private Double groutingTotal;

    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sid;

    @ApiModelProperty(value = "删除状态（0-未删除，1-已删除）")
    private String isDelete;


}
