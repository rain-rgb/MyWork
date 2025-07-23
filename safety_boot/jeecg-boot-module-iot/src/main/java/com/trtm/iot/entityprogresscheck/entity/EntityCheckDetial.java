package com.trtm.iot.entityprogresscheck.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 实体进度清单子表
 * @Author: jeecg-boot
 * @Date:   2022-07-01
 * @Version: V1.0
 */
@ApiModel(value="entity_check_detial对象", description="实体进度清单子表")
@Data
@TableName("entity_check_detial")
public class EntityCheckDetial implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**计划时间(年-月)*/
	@Excel(name = "计划时间(年-月)", width = 15, format = "yyyy-MM")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM")
    @DateTimeFormat(pattern="yyyy-MM")
    @ApiModelProperty(value = "计划时间(年-月)")
    private java.util.Date time;
	/**计划数量*/
	@Excel(name = "计划数量", width = 15)
    @ApiModelProperty(value = "计划数量")
    private java.lang.String planAmount;
	/**计划完成数量*/
	@Excel(name = "计划完成数量", width = 15)
    @ApiModelProperty(value = "计划完成数量")
    private java.lang.String plannedAmount;
	/**唯一码*/
    @ApiModelProperty(value = "唯一码")
    private java.lang.String uuid;
	/**计划完成进度(%)*/
	@Excel(name = "计划完成进度(%)", width = 15)
    @ApiModelProperty(value = "计划完成进度(%)")
    private java.lang.String planProgress;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
}
