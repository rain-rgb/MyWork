package com.trtm.iot.gscsInform.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 班组安全管控系统通知详情表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
@Data
@TableName("gscs_inform")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gscs_inform对象", description="班组安全管控系统通知详情表")
public class GscsInform implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**通知标题*/
	@Excel(name = "通知标题", width = 15)
    @ApiModelProperty(value = "通知标题")
    private java.lang.String informTitle;
	/**通知内容*/
	@Excel(name = "通知内容", width = 15)
    @ApiModelProperty(value = "通知内容")
    private java.lang.String inform;
	/**通知类型（1考试通知， 2其他通知）*/
	@Excel(name = "通知类型（1考试通知， 2其他通知）", width = 15)
    @ApiModelProperty(value = "通知类型（1考试通知， 2其他通知）")
    private java.lang.Integer informType;
	/**接收通知人*/
	@Excel(name = "接收通知人", width = 15)
    @ApiModelProperty(value = "接收通知人")
    private String informTo;
	/**通知视频*/
	@Excel(name = "通知视频", width = 15)
    @ApiModelProperty(value = "通知视频")
    private java.lang.String informVideo;
	/**通知图片*/
	@Excel(name = "通知图片", width = 15)
    @ApiModelProperty(value = "通知图片")
    private java.lang.String informPhoto;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
}
