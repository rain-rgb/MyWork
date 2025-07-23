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
 * @Description: jikeng_tuyaji
 * @Author: jeecg-boot
 * @Date:   2025-02-26
 * @Version: V1.0
 */
@Data
@TableName("jikeng_tuyaji")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jikeng_tuyaji对象", description="jikeng_tuyaji")
public class JikengTuyaji implements Serializable {
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
	/**数值*/
	@Excel(name = "数值", width = 15)
    @ApiModelProperty(value = "数值")
    private java.lang.String depth;
	/**正常/预警原因*/
	@Excel(name = "正常/预警原因", width = 15)
    @ApiModelProperty(value = "正常/预警原因")
    private java.lang.String note;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
	/**理论值水位*/
	@Excel(name = "理论值水位", width = 15)
    @ApiModelProperty(value = "理论值水位")
    private java.lang.String lldepth;
	/**0:基坑/1:围堰*/
	@Excel(name = "0:基坑/1:围堰", width = 15)
    @ApiModelProperty(value = "0:基坑/1:围堰")
    private java.lang.Integer shebeitype;
}
