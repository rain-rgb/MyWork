package com.trtm.iot.largesbconfigure.entity;

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
 * @Description: largesb_history
 * @Author: jeecg-boot
 * @Date:   2023-09-14
 * @Version: V1.0
 */
@Data
@TableName("largesb_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="largesb_history对象", description="largesb_history")
public class LargesbHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**大型设备预警历史记录表*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "大型设备预警历史记录表")
    private java.lang.Integer id;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    @Dict(dicCode = "sbtype")
    private java.lang.Integer sblx;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**预警原因*/
	@Excel(name = "预警原因", width = 15)
    @ApiModelProperty(value = "预警原因")
    private java.lang.String yujingInfo;
	/**预警时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预警时间")
    private java.util.Date time;

    private java.lang.String port1; // 端口1
    private java.lang.String port2;  // 端口2
}
