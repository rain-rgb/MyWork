package com.trtm.iot.jikeng.entity;

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
 * @Description: jikeng_weiyi
 * @Author: jeecg-boot
 * @Date:   2024-05-23
 * @Version: V1.0
 */
@Data
@TableName("jikeng_weiyi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jikeng_weiyi对象", description="jikeng_weiyi")
public class JikengWeiyi implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String devsn;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String devname;
	/**X 轴位移 cm*/
	@Excel(name = "X 轴位移 cm", width = 15)
    @ApiModelProperty(value = "X 轴位移 cm")
    private java.lang.String szx;
	/**Y 轴位移 cm*/
	@Excel(name = "Y 轴位移 cm", width = 15)
    @ApiModelProperty(value = "Y 轴位移 cm")
    private java.lang.String szy;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
	/**正常/预警原因*/
	@Excel(name = "正常/预警原因", width = 15)
    @ApiModelProperty(value = "正常/预警原因")
    private java.lang.String note;
}
