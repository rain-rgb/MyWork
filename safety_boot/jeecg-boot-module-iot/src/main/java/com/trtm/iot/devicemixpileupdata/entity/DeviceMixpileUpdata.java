package com.trtm.iot.devicemixpileupdata.entity;

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
 * @Description: device_mixpile_updata
 * @Author: jeecg-boot
 * @Date:   2021-12-14
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_updata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_updata对象", description="device_mixpile_updata")
public class DeviceMixpileUpdata implements Serializable {
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
	/**水定值*/
	@Excel(name = "水定值", width = 15)
    @ApiModelProperty(value = "水定值")
    private java.lang.String upSet1;
	/**水泥定值*/
	@Excel(name = "水泥定值", width = 15)
    @ApiModelProperty(value = "水泥定值")
    private java.lang.String upSet2;
	/**水计量值*/
	@Excel(name = "水计量值", width = 15)
    @ApiModelProperty(value = "水计量值")
    private java.lang.String upReal1;
	/**水泥计量值*/
	@Excel(name = "水泥计量值", width = 15)
    @ApiModelProperty(value = "水泥计量值")
    private java.lang.String upReal2;
	/**水泥浆比重*/
	@Excel(name = "水泥浆比重", width = 15)
    @ApiModelProperty(value = "水泥浆比重")
    private java.lang.String upDensity;
	/**水灰比*/
	@Excel(name = "水灰比", width = 15)
    @ApiModelProperty(value = "水灰比")
    private java.lang.String upRatio;
	/**生产时间*/
	@Excel(name = "生产时间", width = 15)
    @ApiModelProperty(value = "生产时间")
    private java.lang.String upTime;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer upTs;
}
