package com.trtm.iot.wzliaocangcommand.entity;

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
 * @Description: 料仓门禁表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
@Data
@TableName("wzliaocangcommand")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzliaocangcommand对象", description="料仓门禁表信息")
public class Wzliaocangcommand implements Serializable {
    private static final long serialVersionUID = 1L;

	/**料仓id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "料仓id")
    private java.lang.Integer id;
	/**项目Code*/
	@Excel(name = "项目Code", width = 15)
    @ApiModelProperty(value = "项目Code")
    private java.lang.String code;
	/**料仓序号*/
	@Excel(name = "料仓序号", width = 15)
    @ApiModelProperty(value = "料仓序号")
    private java.lang.Integer indexs;
	/**是否触发命令0不触发1触发*/
	@Excel(name = "是否触发命令0不触发1触发", width = 15)
    @ApiModelProperty(value = "是否触发命令0不触发1触发")
    private java.lang.Integer isdo;
	/**命令内容0：关门，1：开门*/
	@Excel(name = "命令内容0：关门，1：开门", width = 15)
    @ApiModelProperty(value = "命令内容0：关门，1：开门")
    private java.lang.Integer command;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**当前发命令的code*/
	@Excel(name = "当前发命令的code", width = 15)
    @ApiModelProperty(value = "当前发命令的code")
    private java.lang.String onecode;

    private java.lang.String departid;

    private java.lang.String sysOrgCode;
}
