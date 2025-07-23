package com.trtm.iot.cunliang.entity;

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
 * @Description: 存梁工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Data
@TableName("beammanagement_procedure")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="beammanagement_procedure对象", description="存梁工序表信息")
public class BeammanagementProcedure implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**制梁任务单uuid*/
	@Excel(name = "制梁任务单uuid", width = 15)
    @ApiModelProperty(value = "制梁任务单uuid")
    private java.lang.String uuid;
	/**梁所存层*/
	@Excel(name = "梁所存层", width = 15)
    @ApiModelProperty(value = "梁所存层")
    private java.lang.Integer liangceng;
	/**存梁行*/
	@Excel(name = "存梁行", width = 15)
    @ApiModelProperty(value = "存梁行")
    private java.lang.String cunlianghang;
	/**存梁列*/
	@Excel(name = "存梁列", width = 15)
    @ApiModelProperty(value = "存梁列")
    private java.lang.String cunlianglie;
	/**梁场设备编号*/
	@Excel(name = "梁场设备编号", width = 15)
    @ApiModelProperty(value = "梁场设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**存入梁时间*/
	@Excel(name = "存入梁时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "存入梁时间")
    private java.util.Date cuntime;
	/**存入梁监管人*/
	@Excel(name = "存入梁监管人", width = 15)
    @ApiModelProperty(value = "存入梁监管人")
    private java.lang.String cunpeople;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出场时间")
    private java.util.Date chutime;
	/**出场监管人*/
	@Excel(name = "出场监管人", width = 15)
    @ApiModelProperty(value = "出场监管人")
    private java.lang.String chupeople;
    /**梁场台座名称*/
    @Excel(name = "梁场台座名称", width = 15)
    @ApiModelProperty(value = "梁场台座名称")
    private java.lang.String taizuoname;
    @Excel(name = "存梁状态 1 已存入 2已出场", width = 15)
    @ApiModelProperty(value = "存梁状态 1 已存入 2已出场")
    private java.lang.Integer status;

    private java.lang.String remark;
}
