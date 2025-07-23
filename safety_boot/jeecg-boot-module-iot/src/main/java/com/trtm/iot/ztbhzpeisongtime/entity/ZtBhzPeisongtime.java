package com.trtm.iot.ztbhzpeisongtime.entity;

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
 * @Description: 自建拌和站配送考核标准时间表
 * @Author: jeecg-boot
 * @Date:   2023-11-01
 * @Version: V1.0
 */
@Data
@TableName("zt_bhz_peisongtime")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zt_bhz_peisongtime对象", description="自建拌和站配送考核标准时间表")
public class ZtBhzPeisongtime implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**工程类型*/
	@Excel(name = "工程类型", width = 15)
    @ApiModelProperty(value = "工程类型")
    private java.lang.Integer engineeringtype;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private java.lang.String engineering;
	/**基准运距 （Km)*/
	@Excel(name = "基准运距 （Km)", width = 15)
    @ApiModelProperty(value = "基准运距 （Km)")
    private java.lang.String distance;
	/**基准考核时间（分钟）*/
	@Excel(name = "基准考核时间（分钟）", width = 15)
    @ApiModelProperty(value = "基准考核时间（分钟）")
    private java.lang.String assessmenttime;
	/**隧道洞口里程*/
	@Excel(name = "隧道洞口里程", width = 15)
    @ApiModelProperty(value = "隧道洞口里程")
    private java.lang.String mileage;
	/**月末里程*/
	@Excel(name = "月末里程", width = 15)
    @ApiModelProperty(value = "月末里程")
    private java.lang.String endmileage;
	/**增加运距 （Km)*/
	@Excel(name = "增加运距 （Km)", width = 15)
    @ApiModelProperty(value = "增加运距 （Km)")
    private java.lang.String gddistance;
	/**增加考核时间（分钟）*/
	@Excel(name = "增加考核时间（分钟）", width = 15)
    @ApiModelProperty(value = "增加考核时间（分钟）")
    private java.lang.String gdassessmenttime;
	/**综合运距 （Km)*/
	@Excel(name = "综合运距 （Km)", width = 15)
    @ApiModelProperty(value = "综合运距 （Km)")
    private java.lang.String zhdistance;
	/**本期考核时间 （分钟）*/
	@Excel(name = "本期考核时间 （分钟）", width = 15)
    @ApiModelProperty(value = "本期考核时间 （分钟）")
    private java.lang.String bqassessmenttime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
