package com.trtm.sy.sydpssysignature.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: sy_dps_sy_signature
 * @Author: jeecg-boot
 * @Date:   2023-09-13
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_signature")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_signature对象", description="sy_dps_sy_signature")
public class SyDpsSySignature implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键,uuid*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键,uuid")
    private java.lang.String id;
	/**外键，样品编号，sy_dps_sy_sample*/
	@Excel(name = "外键，样品编号，sy_dps_sy_sample", width = 15)
    @ApiModelProperty(value = "外键，样品编号，sy_dps_sy_sample")
    private java.lang.String sampleid;
	/**流程名称*/
	@Excel(name = "流程名称", width = 15)
    @ApiModelProperty(value = "流程名称")
    private java.lang.String liuchengmingcheng;
	/**签章类型 1：施工单位 2：监理单位*/
	@Excel(name = "签章类型 1：施工单位 2：监理单位", width = 15)
    @ApiModelProperty(value = "签章类型 1：施工单位 2：监理单位")
    private java.lang.Integer qianzhangleixing;
	/**流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表*/
	@Excel(name = "流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表", width = 15)
    @ApiModelProperty(value = "流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表")
    private java.lang.Integer liuchengleixing;
	/**记录表签章状态   默认-1：未启动签章*/
	@Excel(name = "记录表签章状态   默认-1：未启动签章", width = 15)
    @ApiModelProperty(value = "记录表签章状态   默认-1：未启动签章")
    private java.lang.String jilubiaoqianzhangzhuangtai;
	/**报告签章状态  默认-1：未启动签章*/
	@Excel(name = "报告签章状态  默认-1：未启动签章", width = 15)
    @ApiModelProperty(value = "报告签章状态  默认-1：未启动签章")
    private java.lang.String baogaoqianzhangzhuangtai;
	/**报验单签章状态  默认-1：未启动签章 0:无报验单        1/3*/
	@Excel(name = "报验单签章状态  默认-1：未启动签章 0:无报验单        1/3", width = 15)
    @ApiModelProperty(value = "报验单签章状态  默认-1：未启动签章 0:无报验单        1/3")
    private java.lang.String baoyandanqianzhangzhuangtai;
	/**审批表签章状态  默认-1：未启动签章 0：无审批表*/
	@Excel(name = "审批表签章状态  默认-1：未启动签章 0：无审批表", width = 15)
    @ApiModelProperty(value = "审批表签章状态  默认-1：未启动签章 0：无审批表")
    private java.lang.String shenpibiaoqianzhangzhuangtai;
	/**外键，用户id，来源t_s_base_user表id字段*/
	@Excel(name = "外键，用户id，来源t_s_base_user表id字段", width = 15)
    @ApiModelProperty(value = "外键，用户id，来源t_s_base_user表id字段")
    private java.lang.String userid;
	/**自定义样品编号（雷榕项目）*/
	@Excel(name = "自定义样品编号（雷榕项目）", width = 15)
    @ApiModelProperty(value = "自定义样品编号（雷榕项目）")
    private java.lang.String sampleno;
	/**自定义报告编号（雷榕项目）*/
	@Excel(name = "自定义报告编号（雷榕项目）", width = 15)
    @ApiModelProperty(value = "自定义报告编号（雷榕项目）")
    private java.lang.String reportno;
	/**样品名称*/
	@Excel(name = "样品名称", width = 15)
    @ApiModelProperty(value = "样品名称")
    private java.lang.String samplename;
	/**工程部位/用途*/
	@Excel(name = "工程部位/用途", width = 15)
    @ApiModelProperty(value = "工程部位/用途")
    private java.lang.String samplegcbw;
	/**外键，组织机构编码（T_S_depart）*/
	@Excel(name = "外键，组织机构编码（T_S_depart）", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码（T_S_depart）")
    private java.lang.String orgcode;
	/**外键，试验项目类型（dps_jc_testItemType表）*/
	@Excel(name = "外键，试验项目类型（dps_jc_testItemType表）", width = 15)
    @ApiModelProperty(value = "外键，试验项目类型（dps_jc_testItemType表）")
    private java.lang.String titcode;
	/**审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）")
    private java.lang.String shenpishijian;
	/**外键，签章流程id，dps_jc_qianzhangliucheng*/
	@Excel(name = "外键，签章流程id，dps_jc_qianzhangliucheng", width = 15)
    @ApiModelProperty(value = "外键，签章流程id，dps_jc_qianzhangliucheng")
    private java.lang.String qianzhangliuchengid;
	/**取样人*/
	@Excel(name = "取样人", width = 15)
    @ApiModelProperty(value = "取样人")
    private java.lang.String samplequyangren;
	/**签章关键字，来源数据字典 shtoone_qzgjz*/
	@Excel(name = "签章关键字，来源数据字典 shtoone_qzgjz", width = 15)
    @ApiModelProperty(value = "签章关键字，来源数据字典 shtoone_qzgjz")
    private java.lang.String qianzhangguanjianzi;
}
