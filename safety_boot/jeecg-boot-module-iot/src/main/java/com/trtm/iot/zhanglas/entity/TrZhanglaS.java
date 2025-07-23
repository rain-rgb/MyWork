package com.trtm.iot.zhanglas.entity;

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
 * @Description: 张拉信息子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla_s对象", description="张拉信息子表")
public class TrZhanglaS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**sid*/
	@Excel(name = "sid", width = 15)
    @ApiModelProperty(value = "sid")
    private java.lang.String sid;
	/**张拉主表主键*/
	@Excel(name = "张拉主表主键", width = 15)
    @ApiModelProperty(value = "张拉主表主键")
    private java.lang.String fguid;
	/**张拉信息表主键*/
	@Excel(name = "张拉信息表主键", width = 15)
    @ApiModelProperty(value = "张拉信息表主键")
    private java.lang.String syjid;
	/**主表的钢束编号*/
	@Excel(name = "主表的钢束编号", width = 15)
    @ApiModelProperty(value = "主表的钢束编号")
    private java.lang.String gsbh;
	/**阶段行程百分比%*/
	@Excel(name = "阶段行程百分比%", width = 15)
    @ApiModelProperty(value = "阶段行程百分比%")
    private java.lang.String jdbfb;
	/**油表读数*/
	@Excel(name = "油表读数", width = 15)
    @ApiModelProperty(value = "油表读数")
    private java.lang.String ybds;
	/**阶段张拉力KN*/
	@Excel(name = "阶段张拉力KN", width = 15)
    @ApiModelProperty(value = "阶段张拉力KN")
    private java.lang.String jdzll;
	/**阶段伸长量mm*/
	@Excel(name = "阶段伸长量mm", width = 15)
    @ApiModelProperty(value = "阶段伸长量mm")
    private java.lang.String jdscl;
	/**顶号（A端或B段）*/
	@Excel(name = "顶号（A端或B段）", width = 15)
    @ApiModelProperty(value = "顶号（A端或B段）")
    private java.lang.String dh;
	/**力筋回缩量*/
	@Excel(name = "力筋回缩量", width = 15)
    @ApiModelProperty(value = "力筋回缩量")
    private java.lang.String ljhsl;
	/**单侧伸长量*/
	@Excel(name = "单侧伸长量", width = 15)
    @ApiModelProperty(value = "单侧伸长量")
    private java.lang.String dcscl;
	/**完成状态(0为未完成,状态为1代表*/
	@Excel(name = "完成状态(0为未完成,状态为1代表", width = 15)
    @ApiModelProperty(value = "完成状态(0为未完成,状态为1代表")
    private java.lang.String status;
	/**持荷时间*/
	@Excel(name = "持荷时间", width = 15)
    @ApiModelProperty(value = "持荷时间")
    private java.lang.String chsj;

}
