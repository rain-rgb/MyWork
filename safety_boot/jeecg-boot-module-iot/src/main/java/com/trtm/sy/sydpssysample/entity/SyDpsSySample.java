package com.trtm.sy.sydpssysample.entity;

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
 * @Description: sy_dps_sy_sample
 * @Author: jeecg-boot
 * @Date:   2023-01-12
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_sample")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_sample对象", description="sy_dps_sy_sample")
public class SyDpsSySample implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键UUID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键UUID")
    private String id;
	/**样品名称*/
	@Excel(name = "样品名称", width = 15)
    @ApiModelProperty(value = "样品名称")
    private String samplename;
	/**样品编号(根据基础配置的编号规则生成)*/
	@Excel(name = "样品编号(根据基础配置的编号规则生成)", width = 15)
    @ApiModelProperty(value = "样品编号(根据基础配置的编号规则生成)")
    private String sampleno;
	/**工程部位/用途*/
	@Excel(name = "工程部位/用途", width = 15)
    @ApiModelProperty(value = "工程部位/用途")
    private String samplegcbw;
	/**样品描述*/
	@Excel(name = "样品描述", width = 15)
    @ApiModelProperty(value = "样品描述")
    private String sampledescribe;
	/**取样日期（yyyy-MM-dd）*/
	@Excel(name = "取样日期（yyyy-MM-dd）", width = 15)
    @ApiModelProperty(value = "取样日期（yyyy-MM-dd）")
    private String sampledate;
	/**产地*/
	@Excel(name = "产地", width = 15)
    @ApiModelProperty(value = "产地")
    private String samplechandi;
	/**代表数量，试验组数*/
	@Excel(name = "代表数量，试验组数", width = 15)
    @ApiModelProperty(value = "代表数量，试验组数")
    private String sampledaibiaoshuliang;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private String sampleguigexinghao;
	/**取样地点/取样位置*/
	@Excel(name = "取样地点/取样位置", width = 15)
    @ApiModelProperty(value = "取样地点/取样位置")
    private String samplequyangdidian;
	/**取样人*/
	@Excel(name = "取样人", width = 15)
    @ApiModelProperty(value = "取样人")
    private String samplequyangren;
	/**生产厂家（水泥，粉煤灰,沥青用到）*/
	@Excel(name = "生产厂家（水泥，粉煤灰,沥青用到）", width = 15)
    @ApiModelProperty(value = "生产厂家（水泥，粉煤灰,沥青用到）")
    private String sampleshengchanchangjia;
	/**出厂编号（水泥样品用到）*/
	@Excel(name = "出厂编号（水泥样品用到）", width = 15)
    @ApiModelProperty(value = "出厂编号（水泥样品用到）")
    private String samplechuchangbianhao;
	/**强度等级，使用数据字典（水泥样品用到）*/
	@Excel(name = "强度等级，使用数据字典（水泥样品用到）", width = 15)
    @ApiModelProperty(value = "强度等级，使用数据字典（水泥样品用到）")
    @Dict(dicCode = "qiangdudengji")
    private String sampleqiangdudengji;
	/**搅拌方式，使用数据字典*/
	@Excel(name = "搅拌方式，使用数据字典", width = 15)
    @ApiModelProperty(value = "搅拌方式，使用数据字典")
    private String samplejiaobanfangshi;
	/**混凝土种类，使用材料字典*/
	@Excel(name = "混凝土种类，使用材料字典", width = 15)
    @ApiModelProperty(value = "混凝土种类，使用材料字典")
    private String samplehunningtuzhonglei;
	/**养护条件，使用材料字典*/
	@Excel(name = "养护条件，使用材料字典", width = 15)
    @ApiModelProperty(value = "养护条件，使用材料字典")
    private String sampleyanghutiaojian;
	/**砂浆种类*/
	@Excel(name = "砂浆种类", width = 15)
    @ApiModelProperty(value = "砂浆种类")
    private String sampleshajiangzhonglei;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String sampleremark;
	/**外键，组织机构id(T_S_depart)*/
	@Excel(name = "外键，组织机构id(T_S_depart)", width = 15)
    @ApiModelProperty(value = "外键，组织机构id(T_S_depart)")
    private String departid;
	/**外键，组织机构编码（T_S_depart）*/
	@Excel(name = "外键，组织机构编码（T_S_depart）", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码（T_S_depart）")
    private String orgcode;
	/**是否删除（0：正常1：已删除）*/
	@Excel(name = "是否删除（0：正常1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：正常1：已删除）")
    private Integer sampleisdel;
	/**创建人（系统登录用户）*/
	@Excel(name = "创建人（系统登录用户）", width = 15)
    @ApiModelProperty(value = "创建人（系统登录用户）")
    private String samplecreateperson;
	/**创建日期（yyyy-MM-dd）*/
	@Excel(name = "创建日期（yyyy-MM-dd）", width = 15)
    @ApiModelProperty(value = "创建日期（yyyy-MM-dd）")
    private String samplecreatedate;
	/**样品状态（0：待检1：在检2：已检）*/
	@Excel(name = "样品状态（0：待检1：在检2：已检）", width = 15)
    @ApiModelProperty(value = "样品状态（0：待检1：在检2：已检）")
    private Integer samplestate;
	/**样品所属机构,当前登录用户所属机构，可选*/
	@Excel(name = "样品所属机构,当前登录用户所属机构，可选", width = 15)
    @ApiModelProperty(value = "样品所属机构,当前登录用户所属机构，可选")
    private String sampledepartid;
	/**生产日期（外加剂，粉煤灰用到）*/
	@Excel(name = "生产日期（外加剂，粉煤灰用到）", width = 15)
    @ApiModelProperty(value = "生产日期（外加剂，粉煤灰用到）")
    private String sampleshengchanriqi;
	/**生产批号（外加剂,粉煤灰用到）*/
	@Excel(name = "生产批号（外加剂,粉煤灰用到）", width = 15)
    @ApiModelProperty(value = "生产批号（外加剂,粉煤灰用到）")
    private String sampleshengchanpihao;
	/**品种等级（石灰，粉煤灰用到）*/
	@Excel(name = "品种等级（石灰，粉煤灰用到）", width = 15)
    @ApiModelProperty(value = "品种等级（石灰，粉煤灰用到）")
    private String samplepinzhongdengji;
	/**设计计量（%）*/
	@Excel(name = "设计计量（%）", width = 15)
    @ApiModelProperty(value = "设计计量（%）")
    private String sampleshejijiliang;
	/**结合料种类*/
	@Excel(name = "结合料种类", width = 15)
    @ApiModelProperty(value = "结合料种类")
    private String samplejieheliaozhonglei;
	/**沥青标号*/
	@Excel(name = "沥青标号", width = 15)
    @ApiModelProperty(value = "沥青标号")
    private String sampleliqingbiaohao;
	/**沥青种类*/
	@Excel(name = "沥青种类", width = 15)
    @ApiModelProperty(value = "沥青种类")
    private String sampleliqingzhonglei;
	/**级配类型*/
	@Excel(name = "级配类型", width = 15)
    @ApiModelProperty(value = "级配类型")
    private String samplejipeileixing;
	/**试验层位*/
	@Excel(name = "试验层位", width = 15)
    @ApiModelProperty(value = "试验层位")
    private String sampleshiyancengwei;
	/**沥青混合料类型*/
	@Excel(name = "沥青混合料类型", width = 15)
    @ApiModelProperty(value = "沥青混合料类型")
    private String sampleliqinghunheliaoleixing;
	/**钢筋直径*/
	@Excel(name = "钢筋直径", width = 15)
    @ApiModelProperty(value = "钢筋直径")
    private String samplegangjinzhijing;
	/**钢筋种类，使用数据字典*/
	@Excel(name = "钢筋种类，使用数据字典", width = 15)
    @ApiModelProperty(value = "钢筋种类，使用数据字典")
    @Dict(dicCode = "gangjinzhonglei")
    private String samplegangjinzhonglei;
	/**龄期 */
	@Excel(name = "龄期 ", width = 15)
    @ApiModelProperty(value = "龄期 ")
    private String samplelingqi;
	/**外加剂掺量*/
	@Excel(name = "外加剂掺量", width = 15)
    @ApiModelProperty(value = "外加剂掺量")
    private String samplewaijiajichanliang;
	/**外键，试验项目类型（dps_jc_testItemType表）*/
	@Excel(name = "外键，试验项目类型（dps_jc_testItemType表）", width = 15)
    @ApiModelProperty(value = "外键，试验项目类型（dps_jc_testItemType表）")
    private String titcode;
	/**现场是否完成试验0：未完成1：已完成 默认给0*/
	@Excel(name = "现场是否完成试验0：未完成1：已完成 默认给0", width = 15)
    @ApiModelProperty(value = "现场是否完成试验0：未完成1：已完成 默认给0")
    private Integer mstatus;
	/**见证取样 0：普通 1：见证取样 默认给0*/
	@Excel(name = "见证取样 0：普通 1：见证取样 默认给0", width = 15)
    @ApiModelProperty(value = "见证取样 0：普通 1：见证取样 默认给0")
    private Integer plusmark;
	/**样品试验组数，默认值（水泥4，水泥混凝土1，钢筋1），水泥混凝土和钢筋在界面允许修改*/
	@Excel(name = "样品试验组数，默认值（水泥4，水泥混凝土1，钢筋1），水泥混凝土和钢筋在界面允许修改", width = 15)
    @ApiModelProperty(value = "样品试验组数，默认值（水泥4，水泥混凝土1，钢筋1），水泥混凝土和钢筋在界面允许修改")
    private String sampleshiyanzushu;
	/**自定义样品编号（雷榕项目）*/
	@Excel(name = "自定义样品编号（雷榕项目）", width = 15)
    @ApiModelProperty(value = "自定义样品编号（雷榕项目）")
    private String samplenonew;
	/**自定义报告编号（雷榕项目）*/
	@Excel(name = "自定义报告编号（雷榕项目）", width = 15)
    @ApiModelProperty(value = "自定义报告编号（雷榕项目）")
    private String reportnonew;
	/**自定义记录编号（雷榕项目）*/
	@Excel(name = "自定义记录编号（雷榕项目）", width = 15)
    @ApiModelProperty(value = "自定义记录编号（雷榕项目）")
    private String tablenumbernew;
	/**报告日期（来源编辑报告时填写的时间）*/
	@Excel(name = "报告日期（来源编辑报告时填写的时间）", width = 15)
    @ApiModelProperty(value = "报告日期（来源编辑报告时填写的时间）")
    private String reportdate;
	/**insertno*/
	@Excel(name = "insertno", width = 15)
    @ApiModelProperty(value = "insertno")
    private String insertno;
	/**试验结果 0：合格，1：不合格*/
	@Excel(name = "试验结果 0：合格，1：不合格", width = 15)
    @ApiModelProperty(value = "试验结果 0：合格，1：不合格")
    private Integer syjg;
	/**不合格原因，由报表自动判断存储*/
	@Excel(name = "不合格原因，由报表自动判断存储", width = 15)
    @ApiModelProperty(value = "不合格原因，由报表自动判断存储")
    private String buhegeyuanyin;
	/**是否留样 0：不留 1：留  默认给0*/
	@Excel(name = "是否留样 0：不留 1：留  默认给0", width = 15)
    @ApiModelProperty(value = "是否留样 0：不留 1：留  默认给0")
    private Integer shifouliuyang;
	/**留样日期（YYYY-MM-dd）如果留样则填写*/
	@Excel(name = "留样日期（YYYY-MM-dd）如果留样则填写", width = 15)
    @ApiModelProperty(value = "留样日期（YYYY-MM-dd）如果留样则填写")
    private String liuyangriqi;
	/**留样期限（天）如果留样则填写*/
	@Excel(name = "留样期限（天）如果留样则填写", width = 15)
    @ApiModelProperty(value = "留样期限（天）如果留样则填写")
    private Integer liuyangqixian;
	/**留样处理情况   如果留样则填写*/
	@Excel(name = "留样处理情况   如果留样则填写", width = 15)
    @ApiModelProperty(value = "留样处理情况   如果留样则填写")
    private String liuyangchuli;
	/**审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回*/
	@Excel(name = "审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回", width = 15)
    @ApiModelProperty(value = "审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回")
    private Integer shenpizhuangtai;
	/**记录表签章状态   默认-1：未启动签章*/
	@Excel(name = "记录表签章状态   默认-1：未启动签章", width = 15)
    @ApiModelProperty(value = "记录表签章状态   默认-1：未启动签章")
    private String jilubiaoqianzhangzhuangtai;
	/**报告签章状态  默认-1：未启动签章*/
	@Excel(name = "报告签章状态  默认-1：未启动签章", width = 15)
    @ApiModelProperty(value = "报告签章状态  默认-1：未启动签章")
    private String baogaoqianzhangzhuangtai;
	/**报验单签章状态  默认-1：未启动签章 0:无报验单        1/3*/
	@Excel(name = "报验单签章状态  默认-1：未启动签章 0:无报验单        1/3", width = 15)
    @ApiModelProperty(value = "报验单签章状态  默认-1：未启动签章 0:无报验单        1/3")
    private String baoyandanqianzhangzhuangtai;
	/**审批表签章状态  默认-1：未启动签章 0：无审批表*/
	@Excel(name = "审批表签章状态  默认-1：未启动签章 0：无审批表", width = 15)
    @ApiModelProperty(value = "审批表签章状态  默认-1：未启动签章 0：无审批表")
    private String shenpibiaoqianzhangzhuangtai;
	/**签章状态 0：未签完 1：已签完*/
	@Excel(name = "签章状态 0：未签完 1：已签完", width = 15)
    @ApiModelProperty(value = "签章状态 0：未签完 1：已签完")
    private Integer qianzhangzhuangtai;
	/**转发状态0：未转 1：已转*/
	@Excel(name = "转发状态0：未转 1：已转", width = 15)
    @ApiModelProperty(value = "转发状态0：未转 1：已转")
    private Integer zhuanfazhuangtai;
	/**外键，分部分项表id,来源dps_jc_project表id*/
	@Excel(name = "外键，分部分项表id,来源dps_jc_project表id", width = 15)
    @ApiModelProperty(value = "外键，分部分项表id,来源dps_jc_project表id")
    private String projectid;
	/**审批人，当前登录用户名*/
	@Excel(name = "审批人，当前登录用户名", width = 15)
    @ApiModelProperty(value = "审批人，当前登录用户名")
    private String shenpiren;
	/**审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）*/
	@Excel(name = "审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）")
    private String shenpishijian;
	/**自定义报验单编号*/
	@Excel(name = "自定义报验单编号", width = 15)
    @ApiModelProperty(value = "自定义报验单编号")
    private String reportingsheetnonew;
	/**自定义审批表编号*/
	@Excel(name = "自定义审批表编号", width = 15)
    @ApiModelProperty(value = "自定义审批表编号")
    private String approvaltablenonew;
	/**委托id*/
	@Excel(name = "委托id", width = 15)
    @ApiModelProperty(value = "委托id")
    private String wtid;
	/**配比通知单ID*/
	@Excel(name = "配比通知单ID", width = 15)
    @ApiModelProperty(value = "配比通知单ID")
    private String phbtzdid;
	/**配比通知单编号*/
	@Excel(name = "配比通知单编号", width = 15)
    @ApiModelProperty(value = "配比通知单编号")
    private String phbtzdbh;
	/**样品经度*/
	@Excel(name = "样品经度", width = 15)
    @ApiModelProperty(value = "样品经度")
    private String samplejidu;
	/**样品纬度*/
	@Excel(name = "样品纬度", width = 15)
    @ApiModelProperty(value = "样品纬度")
    private String sampleweidu;
	/**委托编号*/
	@Excel(name = "委托编号", width = 15)
    @ApiModelProperty(value = "委托编号")
    private String wtbh;
	/**报告日期*/
	@Excel(name = "报告日期", width = 15)
    @ApiModelProperty(value = "报告日期")
    private String baogaoriqi;
	/**产地2*/
	@Excel(name = "产地2", width = 15)
    @ApiModelProperty(value = "产地2")
    private String samplechandi2;
	/**产地3*/
	@Excel(name = "产地3", width = 15)
    @ApiModelProperty(value = "产地3")
    private String samplechandi3;
	/**取样地点2*/
	@Excel(name = "取样地点2", width = 15)
    @ApiModelProperty(value = "取样地点2")
    private String samplequyangdidian2;
	/**取样地点3*/
	@Excel(name = "取样地点3", width = 15)
    @ApiModelProperty(value = "取样地点3")
    private String samplequyangdidian3;
	/**取样日期2*/
	@Excel(name = "取样日期2", width = 15)
    @ApiModelProperty(value = "取样日期2")
    private String sampledate2;
	/**取样日期3*/
	@Excel(name = "取样日期3", width = 15)
    @ApiModelProperty(value = "取样日期3")
    private String sampledate3;
	/**钢筋等级*/
	@Excel(name = "钢筋等级", width = 15)
    @ApiModelProperty(value = "钢筋等级")
    private String samplegangjindengji;
	/**0：已收样 1：已提交 2：已分配  默认2*/
	@Excel(name = "0：已收样 1：已提交 2：已分配  默认2", width = 15)
    @ApiModelProperty(value = "0：已收样 1：已提交 2：已分配  默认2")
    private Integer syzt;
	/**代表数量2*/
	@Excel(name = "代表数量2", width = 15)
    @ApiModelProperty(value = "代表数量2")
    private String sampledaibiaoshuliang2;
	/**代表数量3*/
	@Excel(name = "代表数量3", width = 15)
    @ApiModelProperty(value = "代表数量3")
    private String sampledaibiaoshuliang3;
	/**规格型号2*/
	@Excel(name = "规格型号2", width = 15)
    @ApiModelProperty(value = "规格型号2")
    private String sampleguigexinghao2;
	/**规格型号3*/
	@Excel(name = "规格型号3", width = 15)
    @ApiModelProperty(value = "规格型号3")
    private String sampleguigexinghao3;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String tinames;
	/**公称壁厚*/
	@Excel(name = "公称壁厚", width = 15)
    @ApiModelProperty(value = "公称壁厚")
    private String samplegongchengbihou;
	/**原材进场委托单编号*/
	@Excel(name = "原材进场委托单编号", width = 15)
    @ApiModelProperty(value = "原材进场委托单编号")
    private String ycjcwtdbh;
	/**整改状态*/
	@Excel(name = "整改状态", width = 15)
    @ApiModelProperty(value = "整改状态")
    private String zgzt;
	/**样品数量*/
	@Excel(name = "样品数量", width = 15)
    @ApiModelProperty(value = "样品数量")
    private String sampleyangpinshuliang;
	/**样品数量2*/
	@Excel(name = "样品数量2", width = 15)
    @ApiModelProperty(value = "样品数量2")
    private String sampleyangpinshuliang2;
	/**样品数量3*/
	@Excel(name = "样品数量3", width = 15)
    @ApiModelProperty(value = "样品数量3")
    private String sampleyangpinshuliang3;
	/**长度*/
	@Excel(name = "长度", width = 15)
    @ApiModelProperty(value = "长度")
    private String samplechangdu;
	/**龄期*/
	@Excel(name = "龄期", width = 15)
    @ApiModelProperty(value = "龄期")
    private String samplelingqimax;
	/**代表数量4*/
	@Excel(name = "代表数量4", width = 15)
    @ApiModelProperty(value = "代表数量4")
    private String sampledaibiaoshuliang4;
	/**代表数量5*/
	@Excel(name = "代表数量5", width = 15)
    @ApiModelProperty(value = "代表数量5")
    private String sampledaibiaoshuliang5;
	/**规格型号4*/
	@Excel(name = "规格型号4", width = 15)
    @ApiModelProperty(value = "规格型号4")
    private String sampleguigexinghao4;
	/**规格型号5*/
	@Excel(name = "规格型号5", width = 15)
    @ApiModelProperty(value = "规格型号5")
    private String sampleguigexinghao5;
	/**样品数量4*/
	@Excel(name = "样品数量4", width = 15)
    @ApiModelProperty(value = "样品数量4")
    private String sampleyangpinshuliang4;
	/**样品数量5*/
	@Excel(name = "样品数量5", width = 15)
    @ApiModelProperty(value = "样品数量5")
    private String sampleyangpinshuliang5;
	/**试件尺寸*/
	@Excel(name = "试块尺寸", width = 15)
    @ApiModelProperty(value = "试块尺寸")
    private String shikuaichicun;
	/**留样数量*/
	@Excel(name = "留样数量", width = 15)
    @ApiModelProperty(value = "留样数量")
    private String sampleliuyangshuliang;
	/**质检日期*/
	@Excel(name = "质检日期", width = 15)
    @ApiModelProperty(value = "质检日期")
    private String samplezhijianriqi;
	/**刚保回弹设备编号*/
	@Excel(name = "刚保回弹设备编号", width = 15)
    @ApiModelProperty(value = "刚保回弹设备编号")
    private String rigidrreboundshebeino;
	/**设备厂家*/
	@Excel(name = "设备厂家", width = 15)
    @ApiModelProperty(value = "设备厂家")
    private String rigidrreboundshebeicj;
	/**检测依据*/
	@Excel(name = "检测依据", width = 15)
    @ApiModelProperty(value = "检测依据")
    private String jianceyiju;
    /**判定依据*/
	@Excel(name = "判定依据", width = 15)
    @ApiModelProperty(value = "判定依据")
    private String pandingyiju;
	/**试验类型（0:原材试验 1:现场检测 2:配合比设计 3:砼试验）*/
	@Excel(name = "试验类型（0:原材试验 1:现场检测 2:配合比设计 3:砼试验）", width = 15)
    @ApiModelProperty(value = "试验类型（0:原材试验 1:现场检测 2:配合比设计 3:砼试验）")
    @Dict(dicCode = "titType")
    private Integer tittype;
    private String tino;
    private String pdfurl;
    private int issign;

}
