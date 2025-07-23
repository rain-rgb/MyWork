package com.trtm.sy.wtgl.qyd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
 * @Description: sy_dps_yy_qydlistview
 * @Author: jeecg-boot
 * @Date:   2023-08-25
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_qydlistview")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_qydlistview对象", description="sy_dps_yy_qydlistview")
public class SyDpsYyQydlistview implements Serializable {
    private static final long serialVersionUID = 1L;

	/**取样单流水号*/
	@Excel(name = "取样单流水号", width = 15)
    @ApiModelProperty(value = "取样单流水号")
    private String quyangdanliushuihao;
	/**主键，自增长*/
	@Excel(name = "主键，自增长", width = 15)
    @ApiModelProperty(value = "主键，自增长")
//    @TableId(value = "quyangdanid")
    private Integer quyangdanid;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private String cailiaono;
	/**供应商单位编码*/
	@Excel(name = "供应商单位编码", width = 15)
    @ApiModelProperty(value = "供应商单位编码")
    private String gongyingshangdanweibianma;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private String guigexinghao;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici;
	/**出厂时间*/
	@Excel(name = "出厂时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出厂时间")
    private Date chuchangshijian;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private String jinchangshijian;
	/**使用部位*/
	@Excel(name = "使用部位", width = 15)
    @ApiModelProperty(value = "使用部位")
    private String usePart;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private String shigongdanwei;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private String jianglidanwei;
	/**取样地点*/
	@Excel(name = "取样地点", width = 15)
    @ApiModelProperty(value = "取样地点")
    private String quyangdidian;
	/**成型日期(YYYY-MM-dd)*/
	@Excel(name = "成型日期(YYYY-MM-dd)", width = 15)
    @ApiModelProperty(value = "成型日期(YYYY-MM-dd)")
    private String chengxingriqi;
	/**样品数量*/
	@Excel(name = "样品数量", width = 15)
    @ApiModelProperty(value = "样品数量")
    private String yangpingshuliang;
	/**样品描述*/
	@Excel(name = "样品描述", width = 15)
    @ApiModelProperty(value = "样品描述")
    private String yangpinmiaoshu;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private String jingzhongt;
	/**检测项目*/
	@Excel(name = "检测项目", width = 15)
    @ApiModelProperty(value = "检测项目")
    private String jiancexiangmu;
	/**抽样人员*/
	@Excel(name = "抽样人员", width = 15)
    @ApiModelProperty(value = "抽样人员")
    private String chouyangrenyuan;
	/**外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String jianzhengren;
	/**外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String shouyangren;
	/**主键，自动增长*/
	@Excel(name = "主键，自动增长", width = 15)
    @ApiModelProperty(value = "主键，自动增长")
    private Integer wtdid;
	/**收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0*/
	@Excel(name = "收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0", width = 15)
    @ApiModelProperty(value = "收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0")
    private Integer shouyangzhuangtai;
	/**收样时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "收样时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "收样时间（YYYY-MM-dd HH:mm:ss）")
    private String shouyangshijian;
	/**外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String quyangren;
	/**取样时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "取样时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "取样时间（YYYY-MM-dd HH:mm:ss）")
    private String quyangshijian;
	/**取样状态（0：未取样  1：已取样）默认0*/
	@Excel(name = "取样状态（0：未取样  1：已取样）默认0", width = 15)
    @ApiModelProperty(value = "取样状态（0：未取样  1：已取样）默认0")
    private Integer quyangzhuangtai;
	/**二维码编号(处理后字段，id+序号)*/
	@Excel(name = "二维码编号(处理后字段，id+序号)", width = 15)
    @ApiModelProperty(value = "二维码编号(处理后字段，id+序号)")
    private String erweimabianhao;
	/**机构/部门名称*/
	@Excel(name = "机构/部门名称", width = 15)
    @ApiModelProperty(value = "机构/部门名称")
    private String departName;
	/**id*/
	@Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private Integer yuancaijinchangdengjiid;
	/**样品编号*/
	@Excel(name = "样品编号", width = 15)
    @ApiModelProperty(value = "样品编号")
    private String sampleNo;
	/**试验类型*/
	@Excel(name = "试验类型", width = 15)
    @ApiModelProperty(value = "试验类型")
    private String titCode;
	/**renwudanliushuihao*/
	@Excel(name = "renwudanliushuihao", width = 15)
    @ApiModelProperty(value = "renwudanliushuihao")
    private String renwudanliushuihao;
	/**检测人员*/
	@Excel(name = "检测人员", width = 15)
    @ApiModelProperty(value = "检测人员")
    private String jiancerenyuan;
	/**外委*/
	@Excel(name = "外委", width = 15)
    @ApiModelProperty(value = "外委")
    private Integer waiweizhuangtai;
	/**外键，试验项目类型（dps_jc_testItemType表）*/
	@Excel(name = "外键，试验项目类型（dps_jc_testItemType表）", width = 15)
    @ApiModelProperty(value = "外键，试验项目类型（dps_jc_testItemType表）")
    private String sampletitcode;
	/**委托日期*/
	@Excel(name = "委托日期", width = 15)
    @ApiModelProperty(value = "委托日期")
    private String weituoriqi;
	/**指派时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "指派时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "指派时间（YYYY-MM-dd HH:mm:ss）")
    private String zhipaishijian;

}
