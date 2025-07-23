package com.trtm.iot.rebarFactory.entity;

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
 * @Description: rebar_factory
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
@Data
@TableName("rebar_factory")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rebar_factory对象", description="rebar_factory")
public class RebarFactory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
	/**加工厂编号*/
	@Excel(name = "加工厂编号", width = 15)
    @ApiModelProperty(value = "加工厂编号")
    private java.lang.String factoryId;
	/**加工厂名称*/
	@Excel(name = "加工厂名称", width = 15)
    @ApiModelProperty(value = "加工厂名称")
    private java.lang.String factoryName;
	/**加工厂地址*/
	@Excel(name = "加工厂地址", width = 15)
    @ApiModelProperty(value = "加工厂地址")
    private java.lang.String factoryAddress;
	/**加工厂所属项目*/
	@Excel(name = "加工厂所属项目", width = 15)
    @ApiModelProperty(value = "加工厂所属项目")
    private java.lang.String factoryOrg;
	/**任务编号*/
	@Excel(name = "任务编号", width = 15)
    @ApiModelProperty(value = "任务编号")
    private java.lang.String taskId;
	/**组织机构编码*/
	@Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    private java.lang.String orgCode;
	/**分部分项组织机构编码*/
	@Excel(name = "分部分项组织机构编码", width = 15)
    @ApiModelProperty(value = "分部分项组织机构编码")
    private java.lang.String orgCodes;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**逻辑删除标识*/
	@Excel(name = "逻辑删除标识", width = 15)
    @ApiModelProperty(value = "逻辑删除标识")
    private java.lang.Integer isDeleted;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createPerson;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updatePerson;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
}
