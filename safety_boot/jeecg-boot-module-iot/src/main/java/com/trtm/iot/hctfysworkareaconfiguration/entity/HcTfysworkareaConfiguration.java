package com.trtm.iot.hctfysworkareaconfiguration.entity;

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
 * @Description: 土方压实配置表
 * @Author: jeecg-boot
 * @Date:   2023-11-16
 * @Version: V1.0
 */
@Data
@TableName("hc_tfysworkarea_configuration")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_tfysworkarea_configuration对象", description="土方压实配置表")
public class HcTfysworkareaConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    @Dict(dictTable = "hc_project", dicText = "pjName", dicCode = "pjId")
    private java.lang.String projectid;
	/**标段名称*/
	@Excel(name = "标段名称", width = 15)
    @ApiModelProperty(value = "标段名称")
    @Dict(dictTable = "hc_section", dicText = "sectionName", dicCode = "sectionId")
    private java.lang.String sectionid;
	/**开振占比（%）最小值*/
	@Excel(name = "开振占比（%）最小值", width = 15)
    @ApiModelProperty(value = "开振占比（%）最小值")
    private java.lang.String vibrateratioMin;

    private java.lang.String vibrateratioZon;
	/**开振占比（%）最大值*/
	@Excel(name = "开振占比（%）最大值", width = 15)
    @ApiModelProperty(value = "开振占比（%）最大值")
    private java.lang.String vibrateratioMax;
	/**平均碾压遍数最小值*/
	@Excel(name = "平均碾压遍数最小值", width = 15)
    @ApiModelProperty(value = "平均碾压遍数最小值")
    private java.lang.String timesavgMin;

    private java.lang.String timesavgZon;
	/**平均碾压遍数最大值*/
	@Excel(name = "平均碾压遍数最大值", width = 15)
    @ApiModelProperty(value = "平均碾压遍数最大值")
    private java.lang.String timesavgMax;
	/**平均厚度（m）最小值*/
	@Excel(name = "平均厚度（m）最小值", width = 15)
    @ApiModelProperty(value = "平均厚度（m）最小值")
    private java.lang.String thickavgMin;

    private java.lang.String thickavgZon;
	/**平均厚度（m）最大值*/
	@Excel(name = "平均厚度（m）最大值", width = 15)
    @ApiModelProperty(value = "平均厚度（m）最大值")
    private java.lang.String thickavgMax;
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
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String phone;
    private java.lang.String stauts;

    /**初级号码组*/
    @Excel(name = "初级号码组", width = 15)
    @ApiModelProperty(value = "初级号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String primaryPhones;
    /**中级号码组*/
    @Excel(name = "中级号码组", width = 15)
    @ApiModelProperty(value = "中级号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String middlePhones;
    /**高级号码组*/
    @Excel(name = "高级号码组", width = 15)
    @ApiModelProperty(value = "高级号码组")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String advancedPhones;
}
