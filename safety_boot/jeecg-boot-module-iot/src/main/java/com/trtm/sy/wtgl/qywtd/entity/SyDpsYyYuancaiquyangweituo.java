package com.trtm.sy.wtgl.qywtd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: sy_dps_yy_yuancaiquyangweituo
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_yuancaiquyangweituo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_yuancaiquyangweituo对象", description="sy_dps_yy_yuancaiquyangweituo")
public class SyDpsYyYuancaiquyangweituo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键，自动增长*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键，自动增长")
    private java.lang.Integer id;
	/**外键，原材进场登记表ID*/
	@Excel(name = "外键，原材进场登记表ID", width = 15)
    @ApiModelProperty(value = "外键，原材进场登记表ID")
    private java.lang.Integer yuancaijinchangdengjiid;
	/**取样状态（0：未取样  1：已取样）默认0*/
	@Excel(name = "取样状态（0：未取样  1：已取样）默认0", width = 15)
    @ApiModelProperty(value = "取样状态（0：未取样  1：已取样）默认0")
    private java.lang.Integer quyangzhuangtai;
	/**外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private java.lang.String quyangren;
	/**取样时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "取样时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "取样时间（YYYY-MM-dd HH:mm:ss）")
    private java.lang.String quyangshijian;
	/**外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private java.lang.String shouyangren;
	/**收样时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "收样时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "收样时间（YYYY-MM-dd HH:mm:ss）")
    private java.lang.String shouyangshijian;
	/**收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0*/
	@Excel(name = "收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0", width = 15)
    @ApiModelProperty(value = "收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0")
    private java.lang.Integer shouyangzhuangtai;
	/**外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private java.lang.String jianzhengren;
	/**见证时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "见证时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "见证时间（YYYY-MM-dd HH:mm:ss）")
    private java.lang.String jianzhengshijian;
	/**见证状态（0：未见证  1：已见证）默认0*/
	@Excel(name = "见证状态（0：未见证  1：已见证）默认0", width = 15)
    @ApiModelProperty(value = "见证状态（0：未见证  1：已见证）默认0")
    private java.lang.Integer jianzhengzhuangtai;
	/**委托单编号，唯一*/
	@Excel(name = "委托单编号，唯一", width = 15)
    @ApiModelProperty(value = "委托单编号，唯一")
    private java.lang.String weituodanbianhao;
	/**样品处理方式*/
	@Excel(name = "样品处理方式", width = 15)
    @ApiModelProperty(value = "样品处理方式")
    private java.lang.String yangpinchulifangshi;
	/**取样地点*/
	@Excel(name = "取样地点", width = 15)
    @ApiModelProperty(value = "取样地点")
    private java.lang.String quyangdidian;
	/**报告份数*/
	@Excel(name = "报告份数", width = 15)
    @ApiModelProperty(value = "报告份数")
    private java.lang.Integer baogaofenshu;
	/**检测项目*/
	@Excel(name = "检测项目", width = 15)
    @ApiModelProperty(value = "检测项目")
    private java.lang.String jiancexiangmu;
	/**委托人*/
	@Excel(name = "委托人", width = 15)
    @ApiModelProperty(value = "委托人")
    private java.lang.String weituoren;
	/**委托日期*/
	@Excel(name = "委托日期", width = 15)
    @ApiModelProperty(value = "委托日期")
    private java.lang.String weituoriqi;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private java.lang.String shigongdanwei;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private java.lang.String jianglidanwei;
	/**施工单位合同号*/
	@Excel(name = "施工单位合同号", width = 15)
    @ApiModelProperty(value = "施工单位合同号")
    private java.lang.String shigongdanweihetonghao;
	/**监理单位合同号*/
	@Excel(name = "监理单位合同号", width = 15)
    @ApiModelProperty(value = "监理单位合同号")
    private java.lang.String jianlidanweihetonghao;
	/**外键，监理单位组织机构id*/
	@Excel(name = "外键，监理单位组织机构id", width = 15)
    @ApiModelProperty(value = "外键，监理单位组织机构id")
    private java.lang.String jianlidanweidepartid;
	/**外键，监理单位组织机构编码*/
	@Excel(name = "外键，监理单位组织机构编码", width = 15)
    @ApiModelProperty(value = "外键，监理单位组织机构编码")
    private java.lang.String jianlidanweiorgcode;
	/**外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段")
    private java.lang.String renwuzhixingren;
	/**执行状态（0：未执行 1：执行中 2：已完成）*/
	@Excel(name = "执行状态（0：未执行 1：执行中 2：已完成）", width = 15)
    @ApiModelProperty(value = "执行状态（0：未执行 1：执行中 2：已完成）")
    private java.lang.Integer zhixingzhuangtai;
	/**外键，样品编号*/
	@Excel(name = "外键，样品编号", width = 15)
    @ApiModelProperty(value = "外键，样品编号")
    @TableField(value = "sampleNo")
    private java.lang.String sampleno;
	/**二维码编号(处理后字段，id+序号)*/
	@Excel(name = "二维码编号(处理后字段，id+序号)", width = 15)
    @ApiModelProperty(value = "二维码编号(处理后字段，id+序号)")
    private java.lang.String erweimabianhao;
	/**二维码UUID*/
	@Excel(name = "二维码UUID", width = 15)
    @ApiModelProperty(value = "二维码UUID")
    private java.lang.String erweimaweiyima;
	/**委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托*/
	@Excel(name = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托", width = 15)
    @ApiModelProperty(value = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托")
    private java.lang.Integer weituozhuangtai;
	/**指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private java.lang.String zhipaiquyangren;
	/**指派时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "指派时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "指派时间（YYYY-MM-dd HH:mm:ss）")
    private java.lang.String zhipaishijian;
	/**指派人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "指派人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "指派人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private java.lang.String zhipairen;
	/**指派状态（0：未指派1：已指派）*/
	@Excel(name = "指派状态（0：未指派1：已指派）", width = 15)
    @ApiModelProperty(value = "指派状态（0：未指派1：已指派）")
    private java.lang.Integer zhipaizhuangtai;
	/**指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段*/
	@Excel(name = "指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段", width = 15)
    @ApiModelProperty(value = "指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段")
    private java.lang.String zhipaiquyangrenid;
	/**beizhu*/
	@Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private java.lang.String beizhu;
	/**fujian*/
	@Excel(name = "fujian", width = 15)
    @ApiModelProperty(value = "fujian")
    private java.lang.String fujian;
    /**fujian*/
    @Excel(name = "xiangmumingcheng", width = 15)
    @ApiModelProperty(value = "xiangmumingcheng")
    private java.lang.String xiangmumingcheng;
    /**fujian*/
    @Excel(name = "weituodanwei", width = 15)
    @ApiModelProperty(value = "weituodanwei")
    private java.lang.String weituodanwei;

}
