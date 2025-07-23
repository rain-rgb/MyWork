package com.trtm.iot.mixpileanalysis.entity;

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
 * @Description: 水泥搅拌桩桩记录分析表
 * @Author: jeecg-boot
 * @Date:   2024-01-24
 * @Version: V1.0
 */
@Data
@TableName("mixpile_analysis")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="mixpile_analysis对象", description="水泥搅拌桩桩记录分析表")
public class MixpileAnalysis implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**分层（m）*/
	@Excel(name = "分层（m）", width = 15)
    @ApiModelProperty(value = "分层（m）")
    private java.lang.Integer lamination;
	/**段灰量*/
	@Excel(name = "段灰量", width = 15)
    @ApiModelProperty(value = "段灰量")
    private java.lang.Double mixpileWb;
	/**段灰量得分*/
	@Excel(name = "段灰量得分", width = 15)
    @ApiModelProperty(value = "段灰量得分")
    private java.lang.Double mixpileWbscore;
	/**每点搅拌次数影响*/
	@Excel(name = "每点搅拌次数影响", width = 15)
    @ApiModelProperty(value = "每点搅拌次数影响")
    private java.lang.Double mixpileM;
	/**每点搅拌次数影响得分*/
	@Excel(name = "每点搅拌次数影响得分", width = 15)
    @ApiModelProperty(value = "每点搅拌次数影响得分")
    private java.lang.Double mixpileMscore;
	/**分段记分*/
	@Excel(name = "分段记分", width = 15)
    @ApiModelProperty(value = "分段记分")
    private java.lang.Double segmentedScoring;
	/**合格*/
	@Excel(name = "合格", width = 15)
    @ApiModelProperty(value = "合格")
    private java.lang.String hege;
	/**施工等级判定*/
	@Excel(name = "施工等级判定", width = 15)
    @ApiModelProperty(value = "施工等级判定")
    private java.lang.String mination;
	/**0分层数据1总得分*/
	@Excel(name = "0分层数据1总得分", width = 15)
    @ApiModelProperty(value = "0分层数据1总得分")
    private java.lang.Integer stuts;
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

    private java.lang.String shebeino;
    private java.lang.String pileno;
    private java.lang.String pileMileage;
    private java.lang.String partEndtime;
}
