package com.trtm.iot.sysignperson.entity;

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
 * @Description: sy_signperson
 * @Author: jeecg-boot
 * @Date:   2023-09-22
 * @Version: V1.0
 */
@Data
@TableName("sy_signperson")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_signperson对象", description="sy_signperson")
public class SySignperson implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**样品编号*/
	@Excel(name = "样品编号", width = 15)
    @ApiModelProperty(value = "样品编号")
    private java.lang.String sampleno;
	/**第一位签章人*/
	@Excel(name = "第一位签章人", width = 15)
    @ApiModelProperty(value = "第一位签章人")
    private java.lang.String firstsign;
	/**第二位签章人*/
	@Excel(name = "第二位签章人", width = 15)
    @ApiModelProperty(value = "第二位签章人")
    private java.lang.String secondsign;
	/**第三位签章人*/
	@Excel(name = "第三位签章人", width = 15)
    @ApiModelProperty(value = "第三位签章人")
    private java.lang.String thirdsign;
}
