package com.trtm.iot.liangjiasheguanli.entity;

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
 * @Description: 预支梁架设管理表
 * @Author: jeecg-boot
 * @Date:   2023-07-05
 * @Version: V1.0
 */
@Data
@TableName("liang_jiasheguanli")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="liang_jiasheguanli对象", description="预支梁架设管理表")
public class LiangJiasheguanli implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**桥梁名*/
	@Excel(name = "桥梁名", width = 15)
    @ApiModelProperty(value = "桥梁名")
    private java.lang.String projectname;
	/**跨名*/
	@Excel(name = "跨名", width = 15)
    @ApiModelProperty(value = "跨名")
    private java.lang.String kuaname;
	/**梁片号*/
	@Excel(name = "梁片号", width = 15)
    @ApiModelProperty(value = "梁片号")
    private java.lang.String liangpian;
	/**架设状态*/
	@Excel(name = "架设状态", width = 15)
    @ApiModelProperty(value = "架设状态")
    @Dict(dicCode = "jiashestutis")
    private java.lang.Integer jiashestutis;
	/**计划架梁时间*/
	@Excel(name = "计划架梁时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划架梁时间")
    private java.util.Date jihuaTime;
	/**实际架梁时间*/
	@Excel(name = "实际架梁时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际架梁时间")
    private java.util.Date shijiTime;
	/**进度状态*/
	@Excel(name = "进度状态", width = 15)
    @ApiModelProperty(value = "进度状态")
    @Dict(dicCode = "jindustutis")
    private java.lang.Integer jindustutis;
	/**架设班组*/
	@Excel(name = "架设班组", width = 15)
    @ApiModelProperty(value = "架设班组")
    private java.lang.String jiashebanzu;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    @ApiModelProperty(value = "负责人")
    private java.lang.String fuzheren;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**分部分项*/
	@Excel(name = "分部分项", width = 15)
    @ApiModelProperty(value = "分部分项")
    private java.lang.String orgCode;
	/**任务单*/
	@Excel(name = "任务单", width = 15)
    @ApiModelProperty(value = "任务单")
    private java.lang.String code;
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
}
