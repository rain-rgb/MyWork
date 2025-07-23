package com.trtm.iot.bhzconcretemodel.entity;

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
 * @Description: bhz_concrete_model_detail
 * @Author: jeecg-boot
 * @Date:   2025-02-11
 * @Version: V1.0
 */
@Data
@TableName("bhz_concrete_model_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_concrete_model_detail对象", description="bhz_concrete_model_detail")
public class BhzConcreteModelDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**试块模具名称*/
	@Excel(name = "试块模具名称", width = 15)
    @ApiModelProperty(value = "试块模具名称")
    private java.lang.String name;
	/**尺寸*/
	@Excel(name = "材质", width = 15)
    @ApiModelProperty(value = "材质")
    private java.lang.String chicun;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.String guige;
	/**制模人*/
    @ApiModelProperty(value = "制模人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**制模时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "制模时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**当前任务单号*/
	@Excel(name = "当前任务单号", width = 15)
    @ApiModelProperty(value = "当前任务单号")
    private java.lang.String rwd;
	/**模具编码*/
	@Excel(name = "模具编码", width = 15)
    @ApiModelProperty(value = "模具编码")
    private java.lang.String code;
	/**制模状态10：形成中；20：已完成*/
	@Excel(name = "制模状态10：形成中；20：已完成", width = 15)
    @ApiModelProperty(value = "制模状态10：形成中；20：已完成")
    @Dict(dicCode = "mjzt")
    private java.lang.String smzt;
	/**wbs部位名称*/
	@Excel(name = "wbs部位名称", width = 15)
    @ApiModelProperty(value = "wbs部位名称")
    private java.lang.String wbs;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String note;
    private java.lang.String pic;
    private java.lang.String location;
}
