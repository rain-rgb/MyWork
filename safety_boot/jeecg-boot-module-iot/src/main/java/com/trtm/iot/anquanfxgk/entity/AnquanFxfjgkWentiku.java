package com.trtm.iot.anquanfxgk.entity;

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
 * @Description: anquan_fxfjgk_wentiku
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxfjgk_wentiku")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxfjgk_wentiku对象", description="anquan_fxfjgk_wentiku")
public class AnquanFxfjgkWentiku implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private java.lang.String code;
	/**问题类别*/
	@Excel(name = "问题类别", width = 15)
    @ApiModelProperty(value = "问题类别")
    private java.lang.String questionType;
	/**工程类别*/
	@Excel(name = "工程类别", width = 15)
    @ApiModelProperty(value = "工程类别")
    private java.lang.String engineeringType;
	/**施工工序*/
	@Excel(name = "施工工序", width = 15)
    @ApiModelProperty(value = "施工工序")
    private java.lang.String constructionProcess;
    /**问题性质*/
    @Excel(name = "问题性质", width = 15)
    @ApiModelProperty(value = "问题性质")
    private java.lang.String problemNature;
	/**安全风险项*/
	@Excel(name = "安全风险项", width = 15)
    @ApiModelProperty(value = "安全风险项")
    private java.lang.String securityRisk;
	/**安全风险项内容*/
	@Excel(name = "安全风险项内容", width = 15)
    @ApiModelProperty(value = "安全风险项内容")
    private java.lang.String securityRiskContent;
	/**风险事件*/
	@Excel(name = "风险事件", width = 15)
    @ApiModelProperty(value = "风险事件")
    private java.lang.String riskEvent;
	/**安全风险问题点描述*/
	@Excel(name = "安全风险问题点描述", width = 15)
    @ApiModelProperty(value = "安全风险问题点描述")
    private java.lang.String notes;
	/**分值*/
	@Excel(name = "分值", width = 15)
    @ApiModelProperty(value = "分值")
    private java.lang.String value;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
}
