package com.trtm.iot.skip_car_device.entity;

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
 * @Description: 运输温度设备
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
@Data
@TableName("skip_car_device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="skip_car_device对象", description="运输温度设备")
public class SkipCarDevice implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**站*/
	@Excel(name = "站", width = 15)
    @ApiModelProperty(value = "站")
    private java.lang.String stand;
	/**sn号*/
	@Excel(name = "sn号", width = 15)
    @ApiModelProperty(value = "sn号")
    private java.lang.String sn;
	/**车牌*/
	@Excel(name = "车牌", width = 15)
    @ApiModelProperty(value = "车牌")
    private java.lang.String carNumber;
	/**磁条号码*/
	@Excel(name = "磁条号码", width = 15)
    @ApiModelProperty(value = "磁条号码")
    private java.lang.String magneticStripe;
	/**卡号*/
	@Excel(name = "卡号", width = 15)
    @ApiModelProperty(value = "卡号")
    private java.lang.String cardNumber;
	/**运输温度设备编号*/
	@Excel(name = "运输温度设备编号", width = 15)
    @ApiModelProperty(value = "运输温度设备编号")
    private java.lang.String devicenum;
	/**driver*/
	@Excel(name = "driver", width = 15)
    @ApiModelProperty(value = "driver")
    private java.lang.String driver;
}
