package com.trtm.iot.entityprogresscheck.vo;

import java.util.List;
import com.trtm.iot.entityprogresscheck.entity.EntityProgresscheck;
import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 实体进度清单数据表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
@Data
@ApiModel(value="entity_progresscheckPage对象", description="实体进度清单数据表")
public class EntityProgresscheckPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**权限*/
	@ApiModelProperty(value = "权限")
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

	@ExcelCollection(name="实体进度清单子表")
	@ApiModelProperty(value = "实体进度清单子表")
	private List<EntityCheckDetial> entityCheckDetialList;

}
