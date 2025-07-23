package com.trtm.sy.sylxdps.entity;

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
 * @Description: sy_dps_yy_weiduodanlistview
 * @Author: jeecg-boot
 * @Date:   2023-01-30
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_weiduodanlistview")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_weiduodanlistview对象", description="sy_dps_yy_weiduodanlistview")
public class SyDpsYyWeiduodanlistview implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键，自动增长*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键，自动增长")
    private Integer id;
	/**主键，自动增长*/
	@Excel(name = "主键，自动增长", width = 15)
    @ApiModelProperty(value = "主键，自动增长")
    private Integer wtdid;
	/**外键，原材进场登记表ID*/
	@Excel(name = "外键，原材进场登记表ID", width = 15)
    @ApiModelProperty(value = "外键，原材进场登记表ID")
    private Integer yuancaijinchangdengjiid;
	/**beizhu*/
	@Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private String beizhu;
	/**fujian*/
	@Excel(name = "fujian", width = 15)
    @ApiModelProperty(value = "fujian")
    private String fujian;
	/**委托单编号，唯一*/
	@Excel(name = "委托单编号，唯一", width = 15)
    @ApiModelProperty(value = "委托单编号，唯一")
    private String weituodanbianhao;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private String shigongdanwei;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private String jianglidanwei;
	/**外键，施工单位组织机构编码*/
	@Excel(name = "外键，施工单位组织机构编码", width = 15)
    @ApiModelProperty(value = "外键，施工单位组织机构编码")
    private String jianlidanweiorgcode;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private String weituodanwei;
	/**委托人*/
	@Excel(name = "委托人", width = 15)
    @ApiModelProperty(value = "委托人")
    private String weituoren;
	/**委托日期*/
	@Excel(name = "委托日期", width = 15)
    @ApiModelProperty(value = "委托日期")
    private String weituoriqi;
	/**取样地点*/
	@Excel(name = "取样地点", width = 15)
    @ApiModelProperty(value = "取样地点")
    private String quyangdidian;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String cailiaomingcheng;
	/**guigexinghao*/
	@Excel(name = "guigexinghao", width = 15)
    @ApiModelProperty(value = "guigexinghao")
    private Double guigexinghao;
	/**生产批号*/
	@Excel(name = "生产批号", width = 15)
    @ApiModelProperty(value = "生产批号")
    private String shengchanpihao;
	/**出厂日期（YYYY-MM-dd）*/
	@Excel(name = "出厂日期（YYYY-MM-dd）", width = 15)
    @ApiModelProperty(value = "出厂日期（YYYY-MM-dd）")
    private String chuchangriqi;
	/**进场日期（YYYY-MM-dd）*/
	@Excel(name = "进场日期（YYYY-MM-dd）", width = 15)
    @ApiModelProperty(value = "进场日期（YYYY-MM-dd）")
    private String jinchangriqi;
	/**使用部位*/
	@Excel(name = "使用部位", width = 15)
    @ApiModelProperty(value = "使用部位")
    private String shiyongbuwei;
	/**报告份数*/
	@Excel(name = "报告份数", width = 15)
    @ApiModelProperty(value = "报告份数")
    private Integer baogaofenshu;
	/**样品处理方式*/
	@Excel(name = "样品处理方式", width = 15)
    @ApiModelProperty(value = "样品处理方式")
    private String yangpinchulifangshi;
	/**检测项目*/
	@Excel(name = "检测项目", width = 15)
    @ApiModelProperty(value = "检测项目")
    private String jiancexiangmu;
	/**材料厂家*/
	@Excel(name = "材料厂家", width = 15)
    @ApiModelProperty(value = "材料厂家")
    private String cailiaochangjia;
	/**委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托*/
	@Excel(name = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托", width = 15)
    @ApiModelProperty(value = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托")
    private Integer weituozhuangtai;
	/**指派状态（0：未指派1：已指派）*/
	@Excel(name = "指派状态（0：未指派1：已指派）", width = 15)
    @ApiModelProperty(value = "指派状态（0：未指派1：已指派）")
    private Integer zhipaizhuangtai;
	/**指派人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "指派人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "指派人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String zhipairen;
	/**指派时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "指派时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "指派时间（YYYY-MM-dd HH:mm:ss）")
    private String zhipaishijian;
	/**指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String zhipaiquyangren;
	/**jinchangshuliang*/
	@Excel(name = "jinchangshuliang", width = 15)
    @ApiModelProperty(value = "jinchangshuliang")
    private Double jinchangshuliang;
	/**样品类型*/
	@Excel(name = "样品类型", width = 15)
    @ApiModelProperty(value = "样品类型")
    private String titcode;
	/**外委状态*/
	@Excel(name = "外委状态", width = 15)
    @ApiModelProperty(value = "外委状态")
    private String waiweizhuangtai;
}
