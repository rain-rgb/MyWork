package com.trtm.iot.soslist.entity;

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
 * @Description: sos_list
 * @Author: jeecg-boot
 * @Date:   2024-12-17
 * @Version: V1.0
 */
@Data
@TableName("sos_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sos_list对象", description="sos_list")
public class SosList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**报警记录id*/
	@Excel(name = "报警记录id", width = 15)
    @ApiModelProperty(value = "报警记录id")
    private java.lang.String sId;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private java.lang.String userName;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private java.lang.String deviceId;
	/**location*/
	@Excel(name = "location", width = 15)
    @ApiModelProperty(value = "location")
    private java.lang.String location;
	/**纬度坐标*/
	@Excel(name = "纬度坐标", width = 15)
    @ApiModelProperty(value = "纬度坐标")
    private java.lang.String xPoint;
	/**经度坐标*/
	@Excel(name = "经度坐标", width = 15)
    @ApiModelProperty(value = "经度坐标")
    private java.lang.String yPoint;
	/**报警时间*/
	@Excel(name = "报警时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "报警时间")
    private java.lang.String cTime;
	/**报警类型*/
	@Excel(name = "报警类型", width = 15)
    @ApiModelProperty(value = "报警类型")
    private java.lang.String sosType;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.String status;
	/**adminId*/
	@Excel(name = "adminId", width = 15)
    @ApiModelProperty(value = "adminId")
    private java.lang.String adminId;
	/**报警消息*/
	@Excel(name = "报警消息", width = 15)
    @ApiModelProperty(value = "报警消息")
    private java.lang.String msg;
	/**所在楼层*/
	@Excel(name = "所在楼层", width = 15)
    @ApiModelProperty(value = "所在楼层")
    private java.lang.String floor;
	/**报警图片路径*/
	@Excel(name = "报警图片路径", width = 15)
    @ApiModelProperty(value = "报警图片路径")
    private java.lang.String imgurl;
	/**手机号码*/
	@Excel(name = "手机号码", width = 15)
    @ApiModelProperty(value = "手机号码")
    private java.lang.String mobile;
	/**isReal*/
	@Excel(name = "isReal", width = 15)
    @ApiModelProperty(value = "isReal")
    private java.lang.String isReal;
	/**rTime*/
	@Excel(name = "rTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "rTime")
    private java.lang.String rTime;
}
