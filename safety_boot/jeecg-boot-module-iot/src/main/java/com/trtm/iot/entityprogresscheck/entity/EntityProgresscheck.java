package com.trtm.iot.entityprogresscheck.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 实体进度清单数据表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
@ApiModel(value="entity_progresscheck对象", description="实体进度清单数据表")
@Data
@TableName("entity_progresscheck")
public class EntityProgresscheck implements Serializable {
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
	/**工程类型*/
	@Excel(name = "工程类型", width = 15)
    @ApiModelProperty(value = "工程类型")
    @Dict(dicCode = "projectType")
    private java.lang.String projectType;
	/**设计总数量*/
	@Excel(name = "设计总数量", width = 15)
    @ApiModelProperty(value = "设计总数量")
    private java.lang.String designQuantity;
	/**开累完成总数量*/
	@Excel(name = "开累完成总数量", width = 15)
    @ApiModelProperty(value = "开累完成总数量")
    private java.lang.String finishedAmount;
	/**剩余数量*/
	@Excel(name = "剩余数量", width = 15)
    @ApiModelProperty(value = "剩余数量")
    private java.lang.String remainingAmount;
	/**开累完成进度(%)*/
	@Excel(name = "开累完成进度(%)", width = 15)
    @ApiModelProperty(value = "开累完成进度(%)")
    private java.lang.String schedule;
	/**数量单位*/
	@Excel(name = "数量单位", width = 15)
    @ApiModelProperty(value = "数量单位")
    private java.lang.String quantityUnit;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private java.lang.String unitProject;
	/**是否是关键节点*/
	@Excel(name = "是否是关键节点", width = 15)
    @ApiModelProperty(value = "是否是关键节点")
    @Dict(dicCode = "yn")
    private java.lang.String iskey;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String uuid;
}
