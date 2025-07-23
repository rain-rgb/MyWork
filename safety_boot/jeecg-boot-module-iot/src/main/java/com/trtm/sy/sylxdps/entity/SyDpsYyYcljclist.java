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
 * @Description: sy_dps_yy_ycljclist
 * @Author: jeecg-boot
 * @Date:   2023-01-30
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_ycljclist")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_ycljclist对象", description="sy_dps_yy_ycljclist")
public class SyDpsYyYcljclist implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键，自动增长*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键，自动增长")
    private Integer id;
	/**材料厂家*/
	@Excel(name = "材料厂家", width = 15)
    @ApiModelProperty(value = "材料厂家")
    private String cailiaochangjia;
	/**材料登记人*/
	@Excel(name = "材料登记人", width = 15)
    @ApiModelProperty(value = "材料登记人")
    private String cailiaodengjiren;
	/**样品类型*/
	@Excel(name = "样品类型", width = 15)
    @ApiModelProperty(value = "样品类型")
    private String titcode;
	/**材料登记时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "材料登记时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "材料登记时间（YYYY-MM-dd HH:mm:ss）")
    private String cailiaodengjishijian;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String cailiaomingcheng;
	/**出厂日期（YYYY-MM-dd）*/
	@Excel(name = "出厂日期（YYYY-MM-dd）", width = 15)
    @ApiModelProperty(value = "出厂日期（YYYY-MM-dd）")
    private String chuchangriqi;
	/**存放地点*/
	@Excel(name = "存放地点", width = 15)
    @ApiModelProperty(value = "存放地点")
    private String chunfangdidian;
	/**二维码编号(处理后字段，id+序号)*/
	@Excel(name = "二维码编号(处理后字段，id+序号)", width = 15)
    @ApiModelProperty(value = "二维码编号(处理后字段，id+序号)")
    private String erweimabianhao;
	/**二维码UUID*/
	@Excel(name = "二维码UUID", width = 15)
    @ApiModelProperty(value = "二维码UUID")
    private String erweimaweiyima;
	/**guigexinghao*/
	@Excel(name = "guigexinghao", width = 15)
    @ApiModelProperty(value = "guigexinghao")
    private Double guigexinghao;
	/**检测依据*/
	@Excel(name = "检测依据", width = 15)
    @ApiModelProperty(value = "检测依据")
    private String jianceyiju;
	/**监理抽检（0：否 1：是）默认0*/
	@Excel(name = "监理抽检（0：否 1：是）默认0", width = 15)
    @ApiModelProperty(value = "监理抽检（0：否 1：是）默认0")
    private Integer jianlichoujianzhuangtai;
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
	/**进场日期（YYYY-MM-dd）*/
	@Excel(name = "进场日期（YYYY-MM-dd）", width = 15)
    @ApiModelProperty(value = "进场日期（YYYY-MM-dd）")
    private String jinchangriqi;
	/**jinchangshuliang*/
	@Excel(name = "jinchangshuliang", width = 15)
    @ApiModelProperty(value = "jinchangshuliang")
    private Double jinchangshuliang;
	/**jinchangshuliang1*/
	@Excel(name = "jinchangshuliang1", width = 15)
    @ApiModelProperty(value = "jinchangshuliang1")
    private String jinchangshuliang1;
	/**jinchangshuliang2*/
	@Excel(name = "jinchangshuliang2", width = 15)
    @ApiModelProperty(value = "jinchangshuliang2")
    private String jinchangshuliang2;
	/**评定依据*/
	@Excel(name = "评定依据", width = 15)
    @ApiModelProperty(value = "评定依据")
    private String pandingyiju;
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
	/**外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String renwuzhixingren;
	/**外键，样品编号*/
	@Excel(name = "外键，样品编号", width = 15)
    @ApiModelProperty(value = "外键，样品编号")
    private String sampleno;
	/**生产批号*/
	@Excel(name = "生产批号", width = 15)
    @ApiModelProperty(value = "生产批号")
    private String shengchanpihao;
	/**外键，施工单位组织机构编码*/
	@Excel(name = "外键，施工单位组织机构编码", width = 15)
    @ApiModelProperty(value = "外键，施工单位组织机构编码")
    private String shigongdanweiorgcode;
	/**外键，施工单位组织机构id*/
	@Excel(name = "外键，施工单位组织机构id", width = 15)
    @ApiModelProperty(value = "外键，施工单位组织机构id")
    private String shigongdasnweidepartid;
	/**试验完成期限*/
	@Excel(name = "试验完成期限", width = 15)
    @ApiModelProperty(value = "试验完成期限")
    private String shiyanwanchengqixian;
	/**使用部位*/
	@Excel(name = "使用部位", width = 15)
    @ApiModelProperty(value = "使用部位")
    private String shiyongbuwei;
	/**外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段*/
	@Excel(name = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段", width = 15)
    @ApiModelProperty(value = "外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段")
    private String shouyangren;
	/**收样时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "收样时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "收样时间（YYYY-MM-dd HH:mm:ss）")
    private String shouyangshijian;
	/**收样状态（0：未收样  1：已收样 2：在检）默认0*/
	@Excel(name = "收样状态（0：未收样  1：已收样 2：在检）默认0", width = 15)
    @ApiModelProperty(value = "收样状态（0：未收样  1：已收样 2：在检）默认0")
    private Integer shouyangzhuangtai;
	/**委托单编号，唯一*/
	@Excel(name = "委托单编号，唯一", width = 15)
    @ApiModelProperty(value = "委托单编号，唯一")
    private String weituodanbianhao;
	/**委托状态（0：未委托 1：已委托 2:已取样 3：已收样 4：未执行 5：执行中 6：已完成）*/
	@Excel(name = "委托状态（0：未委托 1：已委托 2:已取样 3：已收样 4：未执行 5：执行中 6：已完成）", width = 15)
    @ApiModelProperty(value = "委托状态（0：未委托 1：已委托 2:已取样 3：已收样 4：未执行 5：执行中 6：已完成）")
    private Integer weituozhuangtai;
	/**质保单（附件）*/
	@Excel(name = "质保单（附件）", width = 15)
    @ApiModelProperty(value = "质保单（附件）")
    private String zhibaodan;
	/**执行状态（0：未执行 1：执行中 2：已完成）*/
	@Excel(name = "执行状态（0：未执行 1：执行中 2：已完成）", width = 15)
    @ApiModelProperty(value = "执行状态（0：未执行 1：执行中 2：已完成）")
    private Integer zhixingzhuangtai;
	/**主键，自动增长*/
	@Excel(name = "主键，自动增长", width = 15)
    @ApiModelProperty(value = "主键，自动增长")
    private Integer wtdid;
	/**委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托*/
	@Excel(name = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托", width = 15)
    @ApiModelProperty(value = "委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托")
    private Integer wtzt;
}
