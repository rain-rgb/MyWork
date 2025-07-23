package com.trtm.iot.devicehammeringhistorydatapart.entity;

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
 * @Description: device_hammering_historydata_part
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
@Data
@TableName("device_hammering_historydata_part")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_hammering_historydata_part对象", description="device_hammering_historydata_part")
public class DeviceHammeringHistorydataPart implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String pileNo;
	/**锤击次数*/
	@Excel(name = "锤击次数", width = 15)
    @ApiModelProperty(value = "锤击次数")
    private java.lang.Integer pileSecond;
	/**深度*/
	@Excel(name = "深度", width = 15)
    @ApiModelProperty(value = "深度")
    private java.lang.String partDep;
	/**垂直度(%)*/
	@Excel(name = "垂直度(%)", width = 15)
    @ApiModelProperty(value = "垂直度(%)")
    private java.lang.String partY;
	/**状态(0 开始 1 接桩 2 压桩 3 结束 )*/
	@Excel(name = "状态(0 开始 1 接桩 2 压桩 3 结束 )", width = 15)
    @ApiModelProperty(value = "状态(0 开始 1 接桩 2 压桩 3 结束 )")
    private java.lang.String partState;
	/**时间*/
	@Excel(name = "时间", width = 15)
    @ApiModelProperty(value = "时间")
    private java.lang.String partEndtime;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer partTs;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String uuid;
	/**阶段电流*/
	@Excel(name = "阶段电流", width = 15)
    @ApiModelProperty(value = "阶段电流")
    private java.lang.String elml;
	/**桩经度*/
	@Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private java.lang.String pileLgd;
	/**桩纬度*/
	@Excel(name = "桩纬度", width = 15)
    @ApiModelProperty(value = "桩纬度")
    private java.lang.String pileLtd;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String pileMileage;
}
