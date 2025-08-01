package com.trtm.iot.comparisonrecord.entity;

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
 * @Description: 评比记录表
 * @Author: jeecg-boot
 * @Date:   2022-07-12
 * @Version: V1.0
 */
@Data
@TableName("comparison_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="comparison_record对象", description="评比记录表")
public class ComparisonRecord implements Serializable {
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
	/**优胜名单*/
	@Excel(name = "优胜名单", width = 15)
    @ApiModelProperty(value = "优胜名单")
    private java.lang.String namewin;
    /**优良名单*/
    @Excel(name = "优良名单", width = 15)
    @ApiModelProperty(value = "优良名单")
    private java.lang.String nameelle;
    /**达标名单*/
    @Excel(name = "达标名单", width = 15)
    @ApiModelProperty(value = "达标名单")
    private java.lang.String namestan;
	/**季度 1 第一季度 2 第二季度 3 第三季度  4 第四季度*/
	@Excel(name = "季度 1 第一季度 2 第二季度 3 第三季度  4 第四季度", width = 15)
    @ApiModelProperty(value = "季度 1 第一季度 2 第二季度 3 第三季度  4 第四季度")
    @Dict(dicCode = "quarter")
    private java.lang.Integer quarter;
	/**优胜数*/
	@Excel(name = "优胜数", width = 15)
    @ApiModelProperty(value = "优胜数")
    private java.lang.Integer winningnum;
	/**优良数*/
	@Excel(name = "优良数", width = 15)
    @ApiModelProperty(value = "优良数")
    private java.lang.Integer excellentnum;
	/**达标数*/
	@Excel(name = "达标数", width = 15)
    @ApiModelProperty(value = "达标数")
    private java.lang.Integer standardnum;
	@Dict(dicCode = "pingbistatus")
	private java.lang.Integer status;//状态 1 达标 2 优良 3 优胜
}
