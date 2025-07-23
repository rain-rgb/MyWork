package com.trtm.iot.bhzcfg.entity;

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
 * @Description: bhz_not_order
 * @Author: jeecg-boot
 * @Date:   2025-04-16
 * @Version: V1.0
 */
@Data
@TableName("bhz_not_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_not_order对象", description="bhz_not_order")
public class BhzNotOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**出料日期*/
	@Excel(name = "出料日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出料日期")
    private java.util.Date chuliaodate;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**标段名称*/
	@Excel(name = "标段名称", width = 15)
    @ApiModelProperty(value = "标段名称")
    private java.lang.String sectionname;
	/**盘数（未使用浇筑令）*/
	@Excel(name = "未使用浇筑令盘数", width = 15)
    @ApiModelProperty(value = "未使用浇筑令盘数")
    private java.lang.String panshu;
	/**生产量（未使用浇筑令））*/
	@Excel(name = "生产量（未使用浇筑令））", width = 15)
    @ApiModelProperty(value = "生产量（未使用浇筑令））")
    private java.lang.String produce;
	/**初级超标数*/
	@Excel(name = "初级超标数", width = 15)
    @ApiModelProperty(value = "初级超标数")
    private java.lang.String chuji;
	/**中级超标数*/
	@Excel(name = "中级超标数", width = 15)
    @ApiModelProperty(value = "中级超标数")
    private java.lang.String zhongji;
	/**高级超标数*/
	@Excel(name = "高级超标数", width = 15)
    @ApiModelProperty(value = "高级超标数")
    private java.lang.String gaoji;
	/**问题原因*/
	@Excel(name = "问题原因", width = 15)
    @ApiModelProperty(value = "问题原因")
    private java.lang.String problemReasons;
	/**填报时间*/
	@Excel(name = "填报时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "填报时间")
    private java.util.Date handleTime;
	/**填报人*/
	@Excel(name = "填报人", width = 15)
    @ApiModelProperty(value = "填报人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String handlePerson;
	/**审批说明*/
	@Excel(name = "审批说明", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审批说明")
    private java.util.Date approvalReasons;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审批时间")
    private java.util.Date approvalTime;
	/**审批人*/
	@Excel(name = "审批人", width = 15)
    @ApiModelProperty(value = "审批人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String approvalPerson;
	/**状态：0为未处理，10为施工方已处理，20为闭合*/
	@Excel(name = "状态：0为未处理，10为施工方已处理，20为闭合", width = 15)
    @ApiModelProperty(value = "状态：0为未处理，10为施工方已处理，20为闭合")
    private java.lang.Integer overproofStatus;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String fileurl;
}
