package com.trtm.iot.guizhouttuisong.entity;

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
 * @Description: guizhout_tuisong
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Data
@TableName("guizhout_tuisong")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="guizhout_tuisong对象", description="guizhout_tuisong")
public class GuizhoutTuisong implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**实际设备编号*/
	@Excel(name = "实际设备编号", width = 15)
    @ApiModelProperty(value = "实际设备编号")
    private java.lang.String sbjno;
	/**实际设备编号*/
	@Excel(name = "实际设备编号", width = 15)
    @ApiModelProperty(value = "实际设备编号")
    private java.lang.String shebeiNo;
	/**标段的guid码*/
	@Excel(name = "标段的guid码", width = 15)
    @ApiModelProperty(value = "标段的guid码")
    private java.lang.String guid;
	/**需要推送的设备名称*/
	@Excel(name = "需要推送的设备名称", width = 15)
    @ApiModelProperty(value = "需要推送的设备名称")
    private java.lang.String sbname;
}
