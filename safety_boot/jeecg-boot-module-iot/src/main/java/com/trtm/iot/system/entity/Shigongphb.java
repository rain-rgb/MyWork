package com.trtm.iot.system.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

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
 * @Description: 施工配合比
 * @Author: jeecg-boot
 * @Date:   2021-05-19
 * @Version: V1.0
 */
@Data
@TableName("shigongphb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="shigongphb对象", description="施工配合比")
public class Shigongphb implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序列号*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序列号")
    private Integer id;
	/**生产线*/
	@Excel(name = "生产线", width = 15)
    @ApiModelProperty(value = "生产线")
    @Dict(dicCode = "station")
    private Integer station;
	/**配合比号*/
	@Excel(name = "配合比号", width = 15)
    @ApiModelProperty(value = "配合比号")
    private String code;
    @Excel(name = "任务单号", width = 15)
    @ApiModelProperty(value = "任务单号")
    private String renwudan;
	/**砼标记*/
	@Excel(name = "砼标记", width = 15)
    @ApiModelProperty(value = "砼标记")
    private String tag;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    @Dict(dicCode = "betlev")
    private String betlev;
	/**抗渗等级*/
	@Excel(name = "抗渗等级", width = 15)
    @ApiModelProperty(value = "抗渗等级")
    private String filter;
	/**抗冻等级*/
	@Excel(name = "抗冻等级", width = 15)
    @ApiModelProperty(value = "抗冻等级")
    private String freeze;
	/**施工季节*/
	@Excel(name = "施工季节", width = 15)
    @ApiModelProperty(value = "施工季节")
    private String season;
	/**坍落度*/
	@Excel(name = "坍落度", width = 15)
    @ApiModelProperty(value = "坍落度")
    @Dict(dicCode = "lands")
    private String lands;
	/**搅拌时间*/
	@Excel(name = "搅拌时间", width = 15)
    @ApiModelProperty(value = "搅拌时间")
    private Integer mixlast;
	/**设计比例*/
	@Excel(name = "设计比例", width = 15)
    @ApiModelProperty(value = "设计比例")
    private String scale;
	/**其他要求*/
	@Excel(name = "其他要求", width = 15)
    @ApiModelProperty(value = "其他要求")
    private String otherreq;
	/**试验员*/
	@Excel(name = "试验员", width = 15)
    @ApiModelProperty(value = "试验员")
    private String exper;
	/**审核员*/
	@Excel(name = "审核员", width = 15)
    @ApiModelProperty(value = "审核员")
    private String assoss;
	/**制定日期*/
	@Excel(name = "制定日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "制定日期")
    private Date dattim;
	/**标识*/
	@Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private String flag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;
	/**原材料1*/
	@Excel(name = "原材料1", width = 15)
    @ApiModelProperty(value = "原材料1")
    @Dict(dicCode = "M1")
    private String m1;
	/**用量1*/
	@Excel(name = "用量1", width = 15)
    @ApiModelProperty(value = "用量1")
    private Double u1;
	/**原材料2*/
	@Excel(name = "原材料2", width = 15)
    @ApiModelProperty(value = "原材料2")
    @Dict(dicCode = "M2")
    private String m2;
	/**用量2*/
	@Excel(name = "用量2", width = 15)
    @ApiModelProperty(value = "用量2")
    private Double u2;
	/**原材料3*/
	@Excel(name = "原材料3", width = 15)
    @ApiModelProperty(value = "原材料3")
    @Dict(dicCode = "M3")
    private String m3;
	/**用量3*/
	@Excel(name = "用量3", width = 15)
    @ApiModelProperty(value = "用量3")
    private Double u3;
	/**原材料4*/
	@Excel(name = "原材料4", width = 15)
    @ApiModelProperty(value = "原材料4")
    @Dict(dicCode = "M4")
    private String m4;
	/**用量4*/
	@Excel(name = "用量4", width = 15)
    @ApiModelProperty(value = "用量4")
    private Double u4;
	/**原材料5*/
	@Excel(name = "原材料5", width = 15)
    @ApiModelProperty(value = "原材料5")
    @Dict(dicCode = "M4")
    private String m5;
	/**用量5*/
	@Excel(name = "用量5", width = 15)
    @ApiModelProperty(value = "用量5")
    private Double u5;
	/**原材料6*/
	@Excel(name = "原材料6", width = 15)
    @ApiModelProperty(value = "原材料6")
    @Dict(dicCode = "M4")
    private String m6;
	/**用量6*/
	@Excel(name = "用量6", width = 15)
    @ApiModelProperty(value = "用量6")
    private Double u6;
	/**原材料7*/
	@Excel(name = "原材料7", width = 15)
    @ApiModelProperty(value = "原材料7")
    @Dict(dicCode = "M4")
    private String m7;
	/**用量7*/
	@Excel(name = "用量7", width = 15)
    @ApiModelProperty(value = "用量7")
    private Double u7;
	/**原材料8*/
	@Excel(name = "原材料8", width = 15)
    @ApiModelProperty(value = "原材料8")
    @Dict(dicCode = "M8")
    private String m8;
	/**用量8*/
	@Excel(name = "用量8", width = 15)
    @ApiModelProperty(value = "用量8")
    private Double u8;
	/**原材料9*/
	@Excel(name = "原材料9", width = 15)
    @ApiModelProperty(value = "原材料9")
    @Dict(dicCode = "M8")
    private String m9;
	/**用量9*/
	@Excel(name = "用量9", width = 15)
    @ApiModelProperty(value = "用量9")
    private Double u9;
	/**原材料10*/
	@Excel(name = "原材料10", width = 15)
    @ApiModelProperty(value = "原材料10")
    @Dict(dicCode = "M8")
    private String m10;
	/**用量10*/
	@Excel(name = "用量10", width = 15)
    @ApiModelProperty(value = "用量10")
    private Double u10;
	/**原材料11*/
	@Excel(name = "原材料11", width = 15)
    @ApiModelProperty(value = "原材料11")
    @Dict(dicCode = "M11")
    private String m11;
	/**用量11*/
	@Excel(name = "用量11", width = 15)
    @ApiModelProperty(value = "用量11")
    private Double u11;
	/**原材料12*/
	@Excel(name = "原材料12", width = 15)
    @ApiModelProperty(value = "原材料12")
    private String m12;
	/**用量12*/
	@Excel(name = "用量12", width = 15)
    @ApiModelProperty(value = "用量12")
    private Double u12;
	/**原材料13*/
	@Excel(name = "原材料13", width = 15)
    @ApiModelProperty(value = "原材料13")
    private String m13;
	/**用量13*/
	@Excel(name = "用量13", width = 15)
    @ApiModelProperty(value = "用量13")
    private Double u13;
	/**原材料14*/
	@Excel(name = "原材料14", width = 15)
    @ApiModelProperty(value = "原材料14")
    private String m14;
	/**用量14*/
	@Excel(name = "用量14", width = 15)
    @ApiModelProperty(value = "用量14")
    private Double u14;
	/**原材料15*/
	@Excel(name = "原材料15", width = 15)
    @ApiModelProperty(value = "原材料15")
    private String m15;
	/**用量15*/
	@Excel(name = "用量15", width = 15)
    @ApiModelProperty(value = "用量15")
    private Double u15;
	/**原材料16*/
	@Excel(name = "原材料16", width = 15)
    @ApiModelProperty(value = "原材料16")
    private String m16;
	/**用量16*/
	@Excel(name = "用量16", width = 15)
    @ApiModelProperty(value = "用量16")
    private Double u16;
	/**原材料17*/
	@Excel(name = "原材料17", width = 15)
    @ApiModelProperty(value = "原材料17")
    private String m17;
	/**用量17*/
	@Excel(name = "用量17", width = 15)
    @ApiModelProperty(value = "用量17")
    private Double u17;
	/**原材料18*/
	@Excel(name = "原材料18", width = 15)
    @ApiModelProperty(value = "原材料18")
    private String m18;
	/**用量18*/
	@Excel(name = "用量18", width = 15)
    @ApiModelProperty(value = "用量18")
    private Double u18;
	/**原材料19*/
	@Excel(name = "原材料19", width = 15)
    @ApiModelProperty(value = "原材料19")
    private String m19;
	/**用量19*/
	@Excel(name = "用量19", width = 15)
    @ApiModelProperty(value = "用量19")
    private Double u19;
	/**原材料20*/
	@Excel(name = "原材料20", width = 15)
    @ApiModelProperty(value = "原材料20")
    private String m20;
	/**用量20*/
	@Excel(name = "用量20", width = 15)
    @ApiModelProperty(value = "用量20")
    private Double u20;
	/**原材料21*/
	@Excel(name = "原材料21", width = 15)
    @ApiModelProperty(value = "原材料21")
    private String m21;
	/**用量21*/
	@Excel(name = "用量21", width = 15)
    @ApiModelProperty(value = "用量21")
    private Double u21;
	/**原材料22*/
	@Excel(name = "原材料22", width = 15)
    @ApiModelProperty(value = "原材料22")
    private String m22;
	/**用量22*/
	@Excel(name = "用量22", width = 15)
    @ApiModelProperty(value = "用量22")
    private Double u22;
	/**原材料23*/
	@Excel(name = "原材料23", width = 15)
    @ApiModelProperty(value = "原材料23")
    private String m23;
	/**用量23*/
	@Excel(name = "用量23", width = 15)
    @ApiModelProperty(value = "用量23")
    private Double u23;
	/**原材料24*/
	@Excel(name = "原材料24", width = 15)
    @ApiModelProperty(value = "原材料24")
    private String m24;
	/**用量24*/
	@Excel(name = "用量24", width = 15)
    @ApiModelProperty(value = "用量24")
    private Double u24;
	/**原材料25*/
	@Excel(name = "原材料25", width = 15)
    @ApiModelProperty(value = "原材料25")
    private String m25;
	/**用量25*/
	@Excel(name = "用量25", width = 15)
    @ApiModelProperty(value = "用量25")
    private Double u25;
	/**原材料26*/
	@Excel(name = "原材料26", width = 15)
    @ApiModelProperty(value = "原材料26")
    private String m26;
	/**用量26*/
	@Excel(name = "用量26", width = 15)
    @ApiModelProperty(value = "用量26")
    private Double u26;
	/**原材料27*/
	@Excel(name = "原材料27", width = 15)
    @ApiModelProperty(value = "原材料27")
    private String m27;
	/**用量27*/
	@Excel(name = "用量27", width = 15)
    @ApiModelProperty(value = "用量27")
    private Double u27;
	/**原材料28*/
	@Excel(name = "原材料28", width = 15)
    @ApiModelProperty(value = "原材料28")
    private String m28;
	/**用量28*/
	@Excel(name = "用量28", width = 15)
    @ApiModelProperty(value = "用量28")
    private Double u28;
	/**原材料29*/
	@Excel(name = "原材料29", width = 15)
    @ApiModelProperty(value = "原材料29")
    private String m29;
	/**用量29*/
	@Excel(name = "用量29", width = 15)
    @ApiModelProperty(value = "用量29")
    private Double u29;
	/**原材料30*/
	@Excel(name = "原材料30", width = 15)
    @ApiModelProperty(value = "原材料30")
    private String m30;
	/**用量30*/
	@Excel(name = "用量30", width = 15)
    @ApiModelProperty(value = "用量30")
    private Double u30;
	/**原材料31*/
	@Excel(name = "原材料31", width = 15)
    @ApiModelProperty(value = "原材料31")
    private String m31;
	/**用量31*/
	@Excel(name = "用量31", width = 15)
    @ApiModelProperty(value = "用量31")
    private Double u31;
	/**原材料32*/
	@Excel(name = "原材料32", width = 15)
    @ApiModelProperty(value = "原材料32")
    private String m32;
	/**用量32*/
	@Excel(name = "用量32", width = 15)
    @ApiModelProperty(value = "用量32")
    private Double u32;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeibianhao;
	/**控制权限*/
    @ApiModelProperty(value = "控制权限")
    private String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
    /**理论配合比号*/
    @Excel(name = "理论配合比号", width = 15)
    @ApiModelProperty(value = "理论配合比号")
    private String code1;
    /**工程名称*/
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String projname;
    /**施工部位*/
    @Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private String conspos;
    /**浇筑方式*/
    @Excel(name = "浇筑方式", width = 15)
    @ApiModelProperty(value = "浇筑方式")
    @Dict(dicCode = "pour")
    private String pour;
    /**含水率原材料4*/
    @Excel(name = "含水率原材料4", width = 15)
    @ApiModelProperty(value = "含水率原材料4")
    private Double mc4;
    /**含水率原材料5*/
    @Excel(name = "含水率原材料5", width = 15)
    @ApiModelProperty(value = "含水率原材料5")
    private Double mc5;
    /**含水率原材料6*/
    @Excel(name = "含水率原材料6", width = 15)
    @ApiModelProperty(value = "含水率原材料6")
    private Double mc6;
    /**含水率原材料7*/
    @Excel(name = "含水率原材料7", width = 15)
    @ApiModelProperty(value = "含水率原材料7")
    private Double mc7;
    /**含水率原材料8*/
    @Excel(name = "含水率原材料8", width = 15)
    @ApiModelProperty(value = "含水率原材料8")
    private Double mc8;
    /**含水率原材料9*/
    @Excel(name = "含水率原材料9", width = 15)
    @ApiModelProperty(value = "含水率原材料9")
    private Double mc9;
    /**含水率原材料10*/
    @Excel(name = "含水率原材料10", width = 15)
    @ApiModelProperty(value = "含水率原材料10")
    private Double mc10;
    /**含水率骨料5*/
    @Excel(name = "含水率骨料5", width = 15)
    @ApiModelProperty(value = "含水率骨料5")
    private Double mc12;
    /**实际用量原材料1*/
    @Excel(name = "原材料1", width = 15)
    @ApiModelProperty(value = "实际用量原材料1")
    private Double ru1;
    /**实际用量原材料2*/
    @Excel(name = "原材料2", width = 15)
    @ApiModelProperty(value = "实际用量原材料2")
    private Double ru2;
    /**实际用量原材料3*/
    @Excel(name = "原材料3", width = 15)
    @ApiModelProperty(value = "实际用量原材料3")
    private Double ru3;
    /**实际用量原材料4*/
    @Excel(name = "原材料4", width = 15)
    @ApiModelProperty(value = "实际用量原材料4")
    private Double ru4;
    /**实际用量原材料5*/
    @Excel(name = "实际用量原材料5", width = 15)
    @ApiModelProperty(value = "实际用量原材料5")
    private Double ru5;
    /**实际用量原材料6*/
    @Excel(name = "实际用量原材料6", width = 15)
    @ApiModelProperty(value = "实际用量原材料6")
    private Double ru6;
    /**实际用量原材料7*/
    @Excel(name = "实际用量原材料7", width = 15)
    @ApiModelProperty(value = "实际用量原材料7")
    private Double ru7;
    /**实际用量原材料8*/
    @Excel(name = "实际用量原材料8", width = 15)
    @ApiModelProperty(value = "实际用量原材料8")
    private Double ru8;
    /**实际用量原材料9*/
    @Excel(name = "实际用量原材料9", width = 15)
    @ApiModelProperty(value = "实际用量原材料9")
    private Double ru9;
    /**实际用量原材料10*/
    @Excel(name = "实际用量原材料10", width = 15)
    @ApiModelProperty(value = "实际用量原材料10")
    private Double ru10;
    /**实际用量原材料11*/
    @Excel(name = "实际用量原材料11", width = 15)
    @ApiModelProperty(value = "实际用量原材料11")
    private Double ru11;
    /**实际用量原材料12*/
    @Excel(name = "实际用量原材料12", width = 15)
    @ApiModelProperty(value = "实际用量原材料12")
    private Double ru12;
    /**实际用量原材料13*/
    @Excel(name = "实际用量原材料13", width = 15)
    @ApiModelProperty(value = "实际用量原材料13")
    private Double ru13;
    /**实际用量原材料14*/
    @Excel(name = "实际用量原材料14", width = 15)
    @ApiModelProperty(value = "实际用量原材料14")
    private Double ru14;
    /**实际用量原材料15*/
    @Excel(name = "实际用量原材料15", width = 15)
    @ApiModelProperty(value = "实际用量原材料15")
    private Double ru15;
    /**含水量（骨料1）*/
    @Excel(name = "含水量（骨料1）", width = 15)
    @ApiModelProperty(value = "含水量（骨料1）")
    private Double mcl4;
    /**含水量（骨料2）*/
    @Excel(name = "含水量（骨料2）", width = 15)
    @ApiModelProperty(value = "含水量（骨料2）")
    private Double mcl5;
    /**含水量（骨料3）*/
    @Excel(name = "含水量（骨料3）", width = 15)
    @ApiModelProperty(value = "含水量（骨料3）")
    private Double mcl6;
    /**含水量（骨料4）*/
    @Excel(name = "含水量（骨料4）", width = 15)
    @ApiModelProperty(value = "含水量（骨料4）")
    private Double mcl7;
    /**含水量（外加剂1）*/
    @Excel(name = "含水量（外加剂1）", width = 15)
    @ApiModelProperty(value = "含水量（外加剂1）")
    private Double mcl8;
    /**含水量（外加剂2）*/
    @Excel(name = "含水量（外加剂2）", width = 15)
    @ApiModelProperty(value = "含水量（外加剂2）")
    private Double mcl9;
    /**含水量（外加剂3）*/
    @Excel(name = "含水量（外加剂3）", width = 15)
    @ApiModelProperty(value = "含水量（外加剂3）")
    private Double mcl10;
    /**含水量（骨料5）*/
    @Excel(name = "含水量（骨料5）", width = 15)
    @ApiModelProperty(value = "含水量（骨料5）")
    private Double mcl12;
    /**任务方量*/
    @Excel(name = "任务方量", width = 15)
    @ApiModelProperty(value = "任务方量")
    private Double mete;
    /**水胶比*/
    @Excel(name = "水胶比", width = 15)
    @ApiModelProperty(value = "水胶比")
    private String waterbindratio;
    /**料仓(水泥)*/
    @Excel(name = "料仓(水泥)", width = 15)
    @ApiModelProperty(value = "料仓(水泥)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc1;
    /**料仓(粉煤灰)*/
    @Excel(name = "料仓(粉煤灰)", width = 15)
    @ApiModelProperty(value = "料仓(粉煤灰)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc2;
    /**料仓(矿粉)*/
    @Excel(name = "料仓(矿粉)", width = 15)
    @ApiModelProperty(value = "料仓(矿粉)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc3;
    /**料仓(骨料1)*/
    @Excel(name = "料仓(骨料1)", width = 15)
    @ApiModelProperty(value = "料仓(骨料1)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc4;
    /**料仓(骨料2)*/
    @Excel(name = "料仓(骨料2)", width = 15)
    @ApiModelProperty(value = "料仓(骨料2)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc5;
    /**料仓(骨料3)*/
    @Excel(name = "料仓(骨料3)", width = 15)
    @ApiModelProperty(value = "料仓(骨料3)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc6;
    /**料仓(骨料4)*/
    @Excel(name = "料仓(骨料4)", width = 15)
    @ApiModelProperty(value = "料仓(骨料4)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc7;
    /**料仓(外加剂1)*/
    @Excel(name = "料仓(外加剂1)", width = 15)
    @ApiModelProperty(value = "料仓(外加剂1)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc8;
    /**料仓(外加剂2)*/
    @Excel(name = "料仓(外加剂2)", width = 15)
    @ApiModelProperty(value = "料仓(外加剂2)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc9;
    /**料仓(外加剂3)*/
    @Excel(name = "料仓(外加剂3)", width = 15)
    @ApiModelProperty(value = "料仓(外加剂3)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc10;
    /**料仓(水)*/
    @Excel(name = "料仓(水)", width = 15)
    @ApiModelProperty(value = "料仓(水)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc11;
    /**料仓(骨料5)*/
    @Excel(name = "料仓(骨料5)", width = 15)
    @ApiModelProperty(value = "料仓(骨料5)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc12;
    /**料仓(水泥2)*/
    @Excel(name = "料仓(水泥2)", width = 15)
    @ApiModelProperty(value = "料仓(水泥2)")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lc13;
    /**是否删除 0未删除 1已删除*/
    @Excel(name = "是否删除 0未删除 1已删除", width = 15)
    @ApiModelProperty(value = "是否删除 0未删除 1已删除")
    private Integer isdel;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici1;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici2;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici3;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici4;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici5;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici6;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici7;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici8;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici9;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici10;
    /**批次*/
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici11;
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici12;
    @Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici13;
    /**制梁任务单编号*/
    @Excel(name = "制梁任务单编号", width = 15)
    @ApiModelProperty(value = "制梁任务单编号")
    private String zhiliangcode;

    @Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
    private String customer;

    @Excel(name = "施工地址", width = 15)
    @ApiModelProperty(value = "施工地址")
    private String projadr;

    @Excel(name = "审核状态（0：审核通过，1：审核未通过）")
    @ApiModelProperty(value = "审核状态（0：审核通过，1：审核未通过）")
    @TableField("check_status")
    @Dict(dicCode = "checkStatus")
    private Integer checkStatus;

    private String phone;
    private Integer statistic;

    private String pici14;// 检验批次(水泥3)
    private String pici15;// 检验批次(水2)
    private String pici16;// 检验批次(备用)
    private String pici17;// 检验批次(备用)
    private String lc14;// 料仓(水泥3)
    private String lc15;// 料仓(水2)
    private String lc16;// 料仓(备用)
    private String lc17;// 料仓(备用)
    private String ru16;// 实际用量（备用）
    private String ru17;// 实际用量（备用）
    private String mc16;// 含水率（备用）
    private String mc17;// 含水率（备用）
    private String mcl16;// 含水量（备用）
    private String mcl17;// 含水量（备用）

    // 是否推送质检资料
    private Integer iszjzl;
    private Date createTime;
    private Integer tongJi;

    @Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String shren;
    @Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    private Date shshijian;
    @Excel(name = "审核意见", width = 15)
    @ApiModelProperty(value = "审核意见")
    private String shyijian;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇筑日期")
    private Date jztime;

    private String piciuse; //未检先用
    private String mtid;

}
