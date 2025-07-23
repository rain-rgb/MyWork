/**
 * @ClassName DevicePulpingHistorydata.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月07日 17:21:00
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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("device_pulping_history_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DevicePulpingHistoryData", description = "六标搅拌站数据")
public class DevicePulpingHistoryData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "实际-水泥")
    private String liao3;

    @ApiModelProperty(value = "实际-粉煤灰")
    private String liao4;

    @ApiModelProperty(value = "理论-水泥")
    private String shejiliao3;

    @ApiModelProperty(value = "理论-粉煤灰")
    private String shejiliao4;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上传时间")
    private Date pulpingdate;


}


