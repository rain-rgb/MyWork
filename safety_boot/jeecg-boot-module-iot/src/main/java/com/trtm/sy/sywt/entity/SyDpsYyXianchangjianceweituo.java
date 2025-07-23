package com.trtm.sy.sywt.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: sy_dps_yy_xianchangjianceweituo
 * @Author: jeecg-boot
 * @Date:   2023-06-28
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_xianchangjianceweituo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_xianchangjianceweituo对象", description="sy_dps_yy_xianchangjianceweituo")
public class SyDpsYyXianchangjianceweituo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键，自动增长*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键，自动增长")
    private Integer id;
	/**外键，原材进场登记表ID*/
	@Excel(name = "外键，原材进场登记表ID", width = 15)
    @ApiModelProperty(value = "外键，原材进场登记表ID")
    @TableField(value = "yuancaijinchangdengjiId")
    private Integer yuancaijinchangdengjiId;
	/**取样状态（0：未取样  1：已取样）默认0*/
	@Excel(name = "取样状态（0：未取样  1：已取样）默认0", width = 15)
    @ApiModelProperty(value = "取样状态（0：未取样  1：已取样）默认0")
    private Integer quyangzhuangtai;
	/**取样人*/
	@Excel(name = "取样人", width = 15)
    @ApiModelProperty(value = "取样人")
    private String quyangren;
	/**取样时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "取样时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "取样时间（YYYY-MM-dd HH:mm:ss）")
    private String quyangshijian;
	/**外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String shouyangren;
	/**收样时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "收样时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "收样时间（YYYY-MM-dd HH:mm:ss）")
    private String shouyangshijian;
	/**收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0*/
	@Excel(name = "收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0", width = 15)
    @ApiModelProperty(value = "收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0")
    private Integer shouyangzhuangtai;
	/**外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String jianzhengren;
	/**见证时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "见证时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "见证时间（YYYY-MM-dd HH:mm:ss）")
    private String jianzhengshijian;
	/**见证状态（0：未见证  1：已见证）默认0*/
	@Excel(name = "见证状态（0：未见证  1：已见证）默认0", width = 15)
    @ApiModelProperty(value = "见证状态（0：未见证  1：已见证）默认0")
    private Integer jianzhengzhuangtai;
	/**委托单编号，唯一*/
	@Excel(name = "委托单编号，唯一", width = 15)
    @ApiModelProperty(value = "委托单编号，唯一")
    private String weituodanbianhao;
	/**样品处理方式*/
	@Excel(name = "样品处理方式", width = 15)
    @ApiModelProperty(value = "样品处理方式")
    private String yangpinchulifangshi;
	/**取样地点*/
	@Excel(name = "取样地点", width = 15)
    @ApiModelProperty(value = "取样地点")
    private String quyangdidian;
	/**报告份数*/
	@Excel(name = "报告份数", width = 15)
    @ApiModelProperty(value = "报告份数")
    private Integer baogaofenshu;
	/**检测项目*/
	@Excel(name = "检测项目", width = 15)
    @ApiModelProperty(value = "检测项目")
    private String jiancexiangmu;
	/**委托人*/
	@Excel(name = "委托人", width = 15)
    @ApiModelProperty(value = "委托人")
    private String weituoren;
	/**委托日期*/
	@Excel(name = "委托日期", width = 15)
    @ApiModelProperty(value = "委托日期")
    private String weituoriqi;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private String shigongdanwei;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private String jianglidanwei;
	/**施工单位合同号*/
	@Excel(name = "施工单位合同号", width = 15)
    @ApiModelProperty(value = "施工单位合同号")
    private String shigongdanweihetonghao;
	/**监理单位合同号*/
	@Excel(name = "监理单位合同号", width = 15)
    @ApiModelProperty(value = "监理单位合同号")
    private String jianlidanweihetonghao;
	/**外键，监理单位组织机构id*/
	@Excel(name = "外键，监理单位组织机构id", width = 15)
    @ApiModelProperty(value = "外键，监理单位组织机构id")
    private String jianlidanweidepartid;
	/**外键，监理单位组织机构编码*/
	@Excel(name = "外键，监理单位组织机构编码", width = 15)
    @ApiModelProperty(value = "外键，监理单位组织机构编码")
    private String jianlidanweiorgcode;
	/**外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String renwuzhixingren;
	/**执行状态（0：未执行 1：执行中 2：已完成）*/
	@Excel(name = "执行状态（0：未执行 1：执行中 2：已完成）", width = 15)
    @ApiModelProperty(value = "执行状态（0：未执行 1：执行中 2：已完成）")
    private Integer zhixingzhuangtai;
	/**外键，样品编号*/
	@Excel(name = "外键，样品编号", width = 15)
    @ApiModelProperty(value = "外键，样品编号")
    private String sampleno;
	/**二维码编号(处理后字段，id+序号)*/
	@Excel(name = "二维码编号(处理后字段，id+序号)", width = 15)
    @ApiModelProperty(value = "二维码编号(处理后字段，id+序号)")
    private String erweimabianhao;
	/**二维码UUID*/
	@Excel(name = "二维码UUID", width = 15)
    @ApiModelProperty(value = "二维码UUID")
    private String erweimaweiyima;
	/**委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托*/
	@Excel(name = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托", width = 15)
    @ApiModelProperty(value = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托")
    private Integer weituozhuangtai;
	/**指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String zhipaiquyangren;
	/**指派时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "指派时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "指派时间（YYYY-MM-dd HH:mm:ss）")
    private String zhipaishijian;
	/**指派人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "指派人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "指派人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String zhipairen;
	/**指派状态（0：未指派1：已指派）*/
	@Excel(name = "指派状态（0：未指派1：已指派）", width = 15)
    @ApiModelProperty(value = "指派状态（0：未指派1：已指派）")
    private Integer zhipaizhuangtai;
	/**指派取样人id*/
	@Excel(name = "指派取样人id", width = 15)
    @ApiModelProperty(value = "指派取样人id")
    private String zhipaiquyangrenid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String beizhu;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private String fujian;
	/**委托单位*/
	@Excel(name = "委托单位", width = 15)
    @ApiModelProperty(value = "委托单位")
    private String weituodanwei;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String cailiaomingcheng;
	/**材料厂家*/
	@Excel(name = "材料厂家", width = 15)
    @ApiModelProperty(value = "材料厂家")
    private String cailiaochangjia;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private String guigexinghao;
	/**样品描述*/
	@Excel(name = "样品描述", width = 15)
    @ApiModelProperty(value = "样品描述")
    @TableField(value = "sampleDescribe")
    private String sampleDescribe;
	/**取样或成型日期*/
	@Excel(name = "取样或成型日期", width = 15)
    @ApiModelProperty(value = "取样或成型日期")
    @TableField(value = "sampleDate")
    private String sampleDate;
	/**样品数量*/
	@Excel(name = "样品数量", width = 15)
    @ApiModelProperty(value = "样品数量")
    @TableField(value = "sampleShuLiang")
    private String sampleShuLiang;
	/**使用部位*/
	@Excel(name = "使用部位", width = 15)
    @ApiModelProperty(value = "使用部位")
    private String shiyongbuwei;
	/**代表数量*/
	@Excel(name = "代表数量", width = 15)
    @ApiModelProperty(value = "代表数量")
    private String daibiaoshuliang;
	/**试验类型编码*/
	@Excel(name = "试验类型编码", width = 15)
    @ApiModelProperty(value = "试验类型编码")
    @TableField(value = "titCode")
    private String titCode;
    private String jianceyiju;
    private String pandingyiju;
    private String shiyanwanchengqixian;
}
