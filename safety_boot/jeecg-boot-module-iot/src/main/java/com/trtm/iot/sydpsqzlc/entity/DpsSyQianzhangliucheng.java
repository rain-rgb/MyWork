package com.trtm.iot.sydpsqzlc.entity;

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
 * @Description: sy_dps_sy_qianzhangliucheng
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_qianzhangliucheng")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dps_sy_qianzhangliucheng对象", description="dps_sy_qianzhangliucheng")
public class DpsSyQianzhangliucheng implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键,uuid*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键,uuid")
    private java.lang.String id;
	/**外键，样品编号，sy_dps_sy_sample*/
	@Excel(name = "外键，样品编号，sy_dps_sy_sample", width = 15)
    @ApiModelProperty(value = "外键，样品编号，sy_dps_sy_sample")
    private java.lang.String sampleid;
	/**外键，签章流程id，dps_jc_qianzhangliucheng*/
	@Excel(name = "外键，签章流程id，dps_jc_qianzhangliucheng", width = 15)
    @ApiModelProperty(value = "外键，签章流程id，dps_jc_qianzhangliucheng")
    private java.lang.String qianzhangliuchengid;
	/**外键，签章id，dps_jc_CAUsbKey*/
	@Excel(name = "外键，签章id，dps_jc_CAUsbKey", width = 15)
    @ApiModelProperty(value = "外键，签章id，dps_jc_CAUsbKey")
    private java.lang.String caid;
	/**签章人，当前登录用户名*/
	@Excel(name = "签章人，当前登录用户名", width = 15)
    @ApiModelProperty(value = "签章人，当前登录用户名")
    private java.lang.String qianzhangren;
	/**签章时间*/
	@Excel(name = "签章时间", width = 15)
    @ApiModelProperty(value = "签章时间")
    private java.lang.String qianzhangshijian;
	/**签章状态 0：未签章 1：已签章 2：已退回*/
	@Excel(name = "签章状态 0：未签章 1：已签章 2：已退回", width = 15)
    @ApiModelProperty(value = "签章状态 0：未签章 1：已签章 2：已退回")
    private java.lang.Integer qianzhangzhuangtai;
	/**签章类型 1：施工单位 2：监理单位*/
	@Excel(name = "签章类型 1：施工单位 2：监理单位", width = 15)
    @ApiModelProperty(value = "签章类型 1：施工单位 2：监理单位")
    private java.lang.Integer qianzhangleixing;
	/**流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表*/
	@Excel(name = "流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表", width = 15)
    @ApiModelProperty(value = "流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表")
    private java.lang.Integer liuchengleixing;
	/**流程状态序号（值等于 流程类型值+填写值）*/
	@Excel(name = "流程状态序号（值等于 流程类型值+填写值）", width = 15)
    @ApiModelProperty(value = "流程状态序号（值等于 流程类型值+填写值）")
    private java.lang.Integer liuchengzhuangtaixuhao;
	/**流程名称*/
	@Excel(name = "流程名称", width = 15)
    @ApiModelProperty(value = "流程名称")
    private java.lang.String liuchengmingcheng;
	/**签章关键字，来源数据字典 shtoone_qzgjz*/
	@Excel(name = "签章关键字，来源数据字典 shtoone_qzgjz", width = 15)
    @ApiModelProperty(value = "签章关键字，来源数据字典 shtoone_qzgjz")
    private java.lang.String qianzhangguanjianzi;
	/**当前流程需要签章页面数量*/
	@Excel(name = "当前流程需要签章页面数量", width = 15)
    @ApiModelProperty(value = "当前流程需要签章页面数量")
    private java.lang.Integer qianzhangyemianshuliang;
	/**evaluateid*/
	@Excel(name = "evaluateid", width = 15)
    @ApiModelProperty(value = "evaluateid")
    private java.lang.String evaluateid;
}
