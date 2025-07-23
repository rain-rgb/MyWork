package com.trtm.iot.virtualgateway.entity;

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
 * @Description: virtual_gateway
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Data
@TableName("virtual_gateway")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="virtual_gateway对象", description="virtual_gateway")
public class VirtualGateway implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String deviceId;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15)
    @ApiModelProperty(value = "采集时间")
    private java.lang.String collectionTime;
	/**布防撤防状态：true：布防，false：撤防*/
	@Excel(name = "布防撤防状态：1-true：布防，0-false：撤防", width = 15)
    @ApiModelProperty(value = "布防撤防状态：true：布防，false：撤防")
    private java.lang.String k;
	/**闯入报警状态：true：报警，false：没有报警*/
	@Excel(name = "闯入报警状态：0-true：报警，0-false：没有报警", width = 15)
    @ApiModelProperty(value = "闯入报警状态：true：报警，false：没有报警")
    private java.lang.String sos;
}
