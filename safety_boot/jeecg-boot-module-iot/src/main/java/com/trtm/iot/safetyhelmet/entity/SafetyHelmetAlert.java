package com.trtm.iot.safetyhelmet.entity;

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
 * @Description: 安全帽数据
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
@Data
@TableName("safety_helmet_alert")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="safety_helmet_alert对象", description="安全帽数据")
public class SafetyHelmetAlert implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**文件全路径*/
	@Excel(name = "文件全路径", width = 15)
    @ApiModelProperty(value = "文件全路径")
    private java.lang.String filename;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date uploadtime;
	/**预警信息*/
	@Excel(name = "预警信息", width = 15)
    @ApiModelProperty(value = "预警信息")
    private java.lang.String alertmsg;
	/**预警发送状态*/
	@Excel(name = "预警发送状态", width = 15)
    @ApiModelProperty(value = "预警发送状态")
    private java.lang.Integer sendstatus;
	/**数据GUID*/
	@Excel(name = "数据GUID", width = 15)
    @ApiModelProperty(value = "数据GUID")
    private java.lang.String guid;
	/**预警消息发送时间*/
	@Excel(name = "预警消息发送时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预警消息发送时间")
    private java.util.Date sendtime;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String imgtype;
    /**ip*/
    @Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private java.lang.String ip;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String safeno;
}
