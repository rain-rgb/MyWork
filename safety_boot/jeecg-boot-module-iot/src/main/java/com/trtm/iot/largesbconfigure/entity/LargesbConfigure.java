package com.trtm.iot.largesbconfigure.entity;

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
 * @Description: largesb_configure
 * @Author: jeecg-boot
 * @Date:   2023-09-12
 * @Version: V1.0
 */
@Data
@TableName("largesb_configure")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="largesb_configure对象", description="largesb_configure")
public class LargesbConfigure implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    @Dict(dicCode = "sbtype")
    private java.lang.Integer sblx;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**预警联系人*/
	@Excel(name = "预警联系人", width = 15)
    @ApiModelProperty(value = "预警联系人")
    private java.lang.String names;
	/**预警号码*/
	@Excel(name = "预警号码", width = 15)
    @ApiModelProperty(value = "预警号码")
    private java.lang.String phones;
	/**是否报警 0:报警 1:不报警*/
	@Excel(name = "是否报警 0:报警 1:不报警", width = 15)
    @ApiModelProperty(value = "是否报警 0:报警 1:不报警")
    private java.lang.Integer yesornot;

    @ApiModelProperty(value = "端口 1端:1 ;2端:2")
    private java.lang.Integer aport;
    /**大型设备门禁机编号*/
    @Excel(name = "大型设备门禁机编号", width = 15)
    @ApiModelProperty(value = "大型设备门禁机编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String doorSbjno;
}
