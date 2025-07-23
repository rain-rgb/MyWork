package com.trtm.iot.materialTesting.entity;

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
 * @Description: 原材料本月检测工作量汇总表
 * @Author: jeecg-boot
 * @Date:   2022-07-15
 * @Version: V1.0
 */
@Data
@TableName("material_testing")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="material_testing对象", description="原材料本月检测工作量汇总表")
public class MaterialTesting implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**权限*/
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**合同段*/
	@Excel(name = "合同段", width = 15)
    @ApiModelProperty(value = "合同段")
    private java.lang.String contractSegment;
	/**检测部位/使用部位*/
	@Excel(name = "检测部位/使用部位", width = 15)
    @ApiModelProperty(value = "检测部位/使用部位")
    private java.lang.String detectionSite;
	/**检测项目*/
	@Excel(name = "检测项目", width = 15)
    @ApiModelProperty(value = "检测项目")
    private java.lang.String testItems;
	/**本月试验次数*/
	@Excel(name = "本月试验次数", width = 15)
    @ApiModelProperty(value = "本月试验次数")
    private java.lang.Integer trialsNum;
	/**本月合格次数(组)*/
	@Excel(name = "本月合格次数(组)", width = 15)
    @ApiModelProperty(value = "本月合格次数(组)")
    private java.lang.Integer passesNum;
	/**本月合格率(%)*/
	@Excel(name = "本月合格率(%)", width = 15)
    @ApiModelProperty(value = "本月合格率(%)")
    private java.lang.Double passRate;
	/**累计检测次数*/
	@Excel(name = "累计检测次数", width = 15)
    @ApiModelProperty(value = "累计检测次数")
    private java.lang.Integer trialsCulnum;
	/**累计检测合格率(%)*/
	@Excel(name = "累计检测合格率(%)", width = 15)
    @ApiModelProperty(value = "累计检测合格率(%)")
    private java.lang.Double passCulrate;
}
