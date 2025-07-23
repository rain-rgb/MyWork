package com.trtm.iot.securitytrain.entity;

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
 * @Description: security_train
 * @Author: jeecg-boot
 * @Date:   2024-08-22
 * @Version: V1.0
 */
@Data
@TableName("security_train")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="security_train对象", description="security_train")
public class SecurityTrain implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
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
	/**培训人*/
	@Excel(name = "培训人", width = 15)
    @ApiModelProperty(value = "培训人")
    private java.lang.String testpeople;
	/**培训内容*/
	@Excel(name = "培训内容", width = 15)
    @ApiModelProperty(value = "培训内容")
    private java.lang.String tescontent;
	/**培训评分*/
	@Excel(name = "培训评分", width = 15)
    @ApiModelProperty(value = "培训评分")
    private java.lang.String result;
	/**考试附件*/
	@Excel(name = "考试附件", width = 15)
    @ApiModelProperty(value = "考试附件")
    private java.lang.String file;
}
