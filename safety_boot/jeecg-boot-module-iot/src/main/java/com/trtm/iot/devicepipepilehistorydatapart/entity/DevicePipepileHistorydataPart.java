package com.trtm.iot.devicepipepilehistorydatapart.entity;

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
 * @Description: device_pipepile_historydata_part
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("device_pipepile_historydata_part")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_pipepile_historydata_part对象", description="device_pipepile_historydata_part")
public class DevicePipepileHistorydataPart implements Serializable {
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
    private String pileNo;
    @Excel(name = "里程桩号", width = 15)
    private java.lang.String pileMileage;// 里程桩号
	/**接桩序号*/
	@Excel(name = "接桩序号", width = 15)
    @ApiModelProperty(value = "接桩序号")
    private String partPilec;
	/**深度*/
	@Excel(name = "深度", width = 15)
    @ApiModelProperty(value = "深度")
    private String partDep;
	/**平均速度(cm/min)*/
	@Excel(name = "平均速度(cm/min)", width = 15)
    @ApiModelProperty(value = "平均速度(cm/min)")
    private String partSpeed;
	/**垂直度(%)*/
	@Excel(name = "垂直度(%)", width = 15)
    @ApiModelProperty(value = "垂直度(%)")
    private String partY;
	/**压桩力(KN)*/
	@Excel(name = "压桩力(KN)", width = 15)
    @ApiModelProperty(value = "压桩力(KN)")
    private String partUpress;
	/**夹持力(KN)*/
	@Excel(name = "夹持力(KN)", width = 15)
    @ApiModelProperty(value = "夹持力(KN)")
    private String partDpress;
	/**状态(0 开始 1 接桩 2 压桩 3 结束 )*/
	@Excel(name = "状态(0 开始 1 接桩 2 压桩 3 结束 )", width = 15)
    @ApiModelProperty(value = "状态(0 开始 1 接桩 2 压桩 3 结束 )")
    private String partState;
	/**时间*/
	@Excel(name = "时间", width = 15)
    @ApiModelProperty(value = "时间")
    private String partEndtime;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private Date datatime;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private Integer partTs;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String uuid;
    /**电流*/
    @Excel(name = "电流", width = 15)
    @ApiModelProperty(value = "电流")
    private String elml;
}
