/**
 * @ClassName LookDeviceTrafficHistroyDataVo.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月22日 14:22:00
 */
package com.trtm.iot.ckqhistorydata.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class LookDeviceTrafficHistoryDataVo {

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private Date datatime;

    @ApiModelProperty(value = "设备编号")
    @Dict(dicCode = "pidreal")
    private String pid;

    @ApiModelProperty(value = "对应的值")
    private Double v;

}


