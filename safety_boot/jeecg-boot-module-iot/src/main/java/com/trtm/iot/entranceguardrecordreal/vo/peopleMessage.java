package com.trtm.iot.entranceguardrecordreal.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class peopleMessage {
    @Excel(name = "doorid", width = 15)
    @ApiModelProperty(value = "doorid")
    private java.lang.String name;

    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String job;

    @Excel(name = "pic", width = 15)
    @ApiModelProperty(value = "pic")
    private java.lang.String pic;

    @Excel(name = "adminid", width = 15)
    @ApiModelProperty(value = "adminid")
    private java.lang.String phone;
}
