package com.trtm.iot.trkongdaogjdxm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 孔道灌浆（定性检测）孔道信息表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@ApiModel(value="tr_kongdaogj_dxkdxx对象", description="孔道灌浆（定性检测）孔道信息表")
@Data
@TableName("tr_kongdaogj_dxkdxx")
public class TrKongdaogjDxkdxx implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**通道号 */
	@Excel(name = "通道号 ", width = 15)
    @ApiModelProperty(value = "通道号 ")
    private java.lang.Integer channelnum;
	/**传感器类型 (0:加速度计1:速度计)*/
	@Excel(name = "传感器类型 (0:加速度计1:速度计)", width = 15)
    @ApiModelProperty(value = "传感器类型 (0:加速度计1:速度计)")
    @Dict(dicCode = "sensortype")
    private java.lang.Integer sensortype;
	/**传感器灵敏度系数*/
	@Excel(name = "传感器灵敏度系数", width = 15)
    @ApiModelProperty(value = "传感器灵敏度系数")
    private java.lang.String sensorsensitive;
	/**积分状态(是否积分, 1:积分 0：不积分)*/
	@Excel(name = "积分状态(是否积分, 1:积分 0：不积分)", width = 15)
    @ApiModelProperty(value = "积分状态(是否积分, 1:积分 0：不积分)")
    @Dict(dicCode = "integralflag")
    private java.lang.Integer integralflag;
	/**基本信息id*/
    @ApiModelProperty(value = "基本信息id")
    private java.lang.Integer zbid;
}
