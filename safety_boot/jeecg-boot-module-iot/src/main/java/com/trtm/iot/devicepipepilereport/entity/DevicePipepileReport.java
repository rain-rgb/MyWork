package com.trtm.iot.devicepipepilereport.entity;

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
 * @Description: device_pipepile_report
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("device_pipepile_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_pipepile_report对象", description="device_pipepile_report")
public class DevicePipepileReport implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private Integer id;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeino;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private String pileno;
	/**接桩序号*/
	@Excel(name = "接桩序号", width = 15)
    @ApiModelProperty(value = "接桩序号")
    private String partPilec;
	/**时长*/
	@Excel(name = "时长", width = 15)
    @ApiModelProperty(value = "时长")
    private String pileWorktime;
	/**接桩时间*/
	@Excel(name = "接桩时间", width = 15)
    @ApiModelProperty(value = "接桩时间")
    private String piletime;
	/**平均速度(cm/min)*/
	@Excel(name = "平均速度(cm/min)", width = 15)
    @ApiModelProperty(value = "平均速度(cm/min)")
    private String speed;
	/**最大垂直度(%)*/
	@Excel(name = "最大垂直度(%)", width = 15)
    @ApiModelProperty(value = "最大垂直度(%)")
    private String pileY;
	/**最大压桩力(KN)*/
	@Excel(name = "最大压桩力(KN)", width = 15)
    @ApiModelProperty(value = "最大压桩力(KN)")
    private String pileUpress;
	/**最大夹持力(KN)*/
	@Excel(name = "最大夹持力(KN)", width = 15)
    @ApiModelProperty(value = "最大夹持力(KN)")
    private String pileDpress;
	/**桩长(m)*/
	@Excel(name = "桩长(m)", width = 15)
    @ApiModelProperty(value = "桩长(m)")
    private String pileDep;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private Date datatime;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private String ts;
}
