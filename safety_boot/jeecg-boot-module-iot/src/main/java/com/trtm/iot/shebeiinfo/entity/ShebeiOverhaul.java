package com.trtm.iot.shebeiinfo.entity;

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
 * @Description: shebei_overhaul
 * @Author: jeecg-boot
 * @Date:   2024-11-13
 * @Version: V1.0
 */
@Data
@TableName("shebei_overhaul")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="shebei_overhaul对象", description="shebei_overhaul")
public class ShebeiOverhaul implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_base", dicText = "shebeiname", dicCode = "shebeino")
    private java.lang.String shebeino;
	/**设备状况描述*/
	@Excel(name = "设备状况描述", width = 15)
    @ApiModelProperty(value = "设备状况描述")
    private java.lang.String miaoshu;
	/**0 正常；1：故障*/
	@Excel(name = "0 正常；1：故障", width = 15)
    @ApiModelProperty(value = "0 正常；1：故障")
    private java.lang.Integer zhengchang;
	/**检查内容*/
	@Excel(name = "检查内容", width = 15)
    @ApiModelProperty(value = "检查内容")
    private java.lang.String checkcontent;
	/**检查方式*/
	@Excel(name = "检查方式", width = 15)
    @ApiModelProperty(value = "检查方式")
    private java.lang.String checkway;
	/**设备照片*/
	@Excel(name = "设备照片", width = 15)
    @ApiModelProperty(value = "设备照片")
    private java.lang.String checkpic;
	/**处理方式*/
	@Excel(name = "处理方式", width = 15)
    @ApiModelProperty(value = "处理方式")
    private java.lang.String handleway;
	/**处理照片*/
	@Excel(name = "处理照片", width = 15)
    @ApiModelProperty(value = "处理照片")
    private java.lang.String handlepic;
	/**处理结果*/
	@Excel(name = "处理结果", width = 15)
    @ApiModelProperty(value = "处理结果")
    private java.lang.String handlereslut;
	/**创建人【检查人】*/
    @ApiModelProperty(value = "创建人【检查人】")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**处理人【维修人】*/
	@Excel(name = "处理人【维修人】", width = 15)
    @ApiModelProperty(value = "处理人【维修人】")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String chuliren;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date chulitime;
	/**备注*/
	@Excel(name = "是否维修", width = 15)
    @ApiModelProperty(value = "是否维修")
    private java.lang.String note;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
