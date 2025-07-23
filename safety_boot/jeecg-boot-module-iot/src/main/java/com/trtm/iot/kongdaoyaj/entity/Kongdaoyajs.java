package com.trtm.iot.kongdaoyaj.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 孔道灌浆子表
 * @Author: jeecg-boot
 * @Date:   2024-06-25
 * @Version: V1.0
 */
@ApiModel(value="kongdaoyajs对象", description="孔道灌浆子表")
@Data
@TableName("kongdaoyajs")
public class Kongdaoyajs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**数据内容*/
	@Excel(name = "数据内容", width = 15)
    @ApiModelProperty(value = "数据内容")
    private java.lang.String detectdata;
	/**检测内容*/
	@Excel(name = "检测内容", width = 15)
    @ApiModelProperty(value = "检测内容")
    private java.lang.String detectitem;
	/**检测编号*/
	@Excel(name = "检测编号", width = 15)
    @ApiModelProperty(value = "检测编号")
    private java.lang.Integer detectitemnum;
	/**解析编号*/
	@Excel(name = "解析编号", width = 15)
    @ApiModelProperty(value = "解析编号")
    private java.lang.String analysisnum;
	/**检测日期*/
	@Excel(name = "检测日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "检测日期")
    private java.util.Date detectdate;
	/**构件编号*/
	@Excel(name = "构件编号", width = 15)
    @ApiModelProperty(value = "构件编号")
    private java.lang.String componentid;
	/**构件名称*/
	@Excel(name = "构件名称", width = 15)
    @ApiModelProperty(value = "构件名称")
    private java.lang.String componentname;
	/**构件位置*/
	@Excel(name = "构件位置", width = 15)
    @ApiModelProperty(value = "构件位置")
    private java.lang.String detectlocation;
	/**数据文件名称*/
	@Excel(name = "数据文件名称", width = 15)
    @ApiModelProperty(value = "数据文件名称")
    private java.lang.String filename;
	/**json文件唯一标识*/
    @ApiModelProperty(value = "json文件唯一标识")
    private java.lang.String uuid;
}
