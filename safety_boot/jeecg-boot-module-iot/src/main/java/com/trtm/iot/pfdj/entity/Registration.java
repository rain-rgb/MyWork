package com.trtm.iot.pfdj.entity;

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
 * @Description: registration
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Data
@TableName("registration")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="registration对象", description="registration")
public class Registration implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**实验室名称*/
	@Excel(name = "实验室名称", width = 15)
    @ApiModelProperty(value = "实验室名称")
    private java.lang.String sysName;
	/**评分表名称*/
	@Excel(name = "评分表名称", width = 15)
    @ApiModelProperty(value = "评分表名称")
    private java.lang.String evaluationName;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**年份*/
	@Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private java.lang.String year;
	/**季度*/
	@Excel(name = "季度", width = 15)
    @ApiModelProperty(value = "季度")
    private java.lang.String season;
	/**人员管理扣分原因*/
	@Excel(name = "人员管理扣分原因", width = 15)
    @ApiModelProperty(value = "人员管理扣分原因")
    private java.lang.String personnelReason;
	/**人员管理扣分数*/
	@Excel(name = "人员管理扣分数", width = 15)
    @ApiModelProperty(value = "人员管理扣分数")
    private java.lang.Integer personnelNum;
	/**仪器设备扣分原因*/
	@Excel(name = "仪器设备扣分原因", width = 15)
    @ApiModelProperty(value = "仪器设备扣分原因")
    private java.lang.String shebeiReason;
	/**仪器设备扣分数*/
	@Excel(name = "仪器设备扣分数", width = 15)
    @ApiModelProperty(value = "仪器设备扣分数")
    private java.lang.Integer shebeiNum;
	/**实验环境扣分原因*/
	@Excel(name = "实验环境扣分原因", width = 15)
    @ApiModelProperty(value = "实验环境扣分原因")
    private java.lang.String environmentReason;
	/**实验环境扣分数*/
	@Excel(name = "实验环境扣分数", width = 15)
    @ApiModelProperty(value = "实验环境扣分数")
    private java.lang.Integer environmentNum;
	/**检测行为扣分原因*/
	@Excel(name = "检测行为扣分原因", width = 15)
    @ApiModelProperty(value = "检测行为扣分原因")
    private java.lang.String behaviorReason;
	/**检测行为扣分数*/
	@Excel(name = "检测行为扣分数", width = 15)
    @ApiModelProperty(value = "检测行为扣分数")
    private java.lang.Integer behaviorNum;
	/**内业资料扣分原因*/
	@Excel(name = "内业资料扣分原因", width = 15)
    @ApiModelProperty(value = "内业资料扣分原因")
    private java.lang.String informationReason;
	/**内业资料扣分数*/
	@Excel(name = "内业资料扣分数", width = 15)
    @ApiModelProperty(value = "内业资料扣分数")
    private java.lang.Integer informationNum;
	/**体系管理扣分原因*/
	@Excel(name = "体系管理扣分原因", width = 15)
    @ApiModelProperty(value = "体系管理扣分原因")
    private java.lang.String systemReason;
	/**体系管理扣分数*/
	@Excel(name = "体系管理扣分数", width = 15)
    @ApiModelProperty(value = "体系管理扣分数")
    private java.lang.Integer systemNum;
    /**数据时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date dataTime;
}
