package com.trtm.iot.hntspjd.entity;

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
 * @Description: 混凝土首盘鉴定表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Data
@TableName("shigongphb_tappraisal")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="shigongphb_tappraisal对象", description="混凝土首盘鉴定表信息")
public class ShigongphbTappraisal implements Serializable {
    private static final long serialVersionUID = 1L;

    /**序列号*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序列号")
    private java.lang.Integer id;
	/**生产线*/
	@Excel(name = "生产线", width = 15)
    @ApiModelProperty(value = "生产线")
    private java.lang.Integer station;
	/**配合比号*/
	@Excel(name = "配合比号", width = 15)
    @ApiModelProperty(value = "配合比号")
    private java.lang.String code;
	/**砼标记*/
	@Excel(name = "砼标记", width = 15)
    @ApiModelProperty(value = "砼标记")
    private java.lang.String tag;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private java.lang.String betlev;
	/**抗渗等级*/
	@Excel(name = "抗渗等级", width = 15)
    @ApiModelProperty(value = "抗渗等级")
    private java.lang.String filters;
	/**抗冻等级*/
	@Excel(name = "抗冻等级", width = 15)
    @ApiModelProperty(value = "抗冻等级")
    private java.lang.String freeze;
	/**施工季节*/
	@Excel(name = "施工季节", width = 15)
    @ApiModelProperty(value = "施工季节")
    private java.lang.String season;
	/**坍落度*/
	@Excel(name = "坍落度", width = 15)
    @ApiModelProperty(value = "坍落度")
    private java.lang.String lands;
	/**搅拌时间*/
	@Excel(name = "搅拌时间", width = 15)
    @ApiModelProperty(value = "搅拌时间")
    private java.lang.Integer mixlast;
	/**设计比例*/
	@Excel(name = "设计比例", width = 15)
    @ApiModelProperty(value = "设计比例")
    private java.lang.String scale;
	/**其他要求*/
	@Excel(name = "其他要求", width = 15)
    @ApiModelProperty(value = "其他要求")
    private java.lang.String otherreq;
	/**试验员*/
	@Excel(name = "试验员", width = 15)
    @ApiModelProperty(value = "试验员")
    private java.lang.String exper;
	/**审核员*/
	@Excel(name = "审核员", width = 15)
    @ApiModelProperty(value = "审核员")
    private java.lang.String assoss;
	/**制定日期*/
	@Excel(name = "制定日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "制定日期")
    private java.util.Date dattim;
	/**标识*/
	@Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private java.lang.String flag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**原材料1*/
	@Excel(name = "原材料1", width = 15)
    @ApiModelProperty(value = "原材料1")
    private java.lang.String m1;
	/**用量1*/
	@Excel(name = "用量1", width = 15)
    @ApiModelProperty(value = "用量1")
    private java.lang.Double u1;
	/**原材料2*/
	@Excel(name = "原材料2", width = 15)
    @ApiModelProperty(value = "原材料2")
    private java.lang.String m2;
	/**用量2*/
	@Excel(name = "用量2", width = 15)
    @ApiModelProperty(value = "用量2")
    private java.lang.Double u2;
	/**原材料3*/
	@Excel(name = "原材料3", width = 15)
    @ApiModelProperty(value = "原材料3")
    private java.lang.String m3;
	/**用量3*/
	@Excel(name = "用量3", width = 15)
    @ApiModelProperty(value = "用量3")
    private java.lang.Double u3;
	/**原材料4*/
	@Excel(name = "原材料4", width = 15)
    @ApiModelProperty(value = "原材料4")
    private java.lang.String m4;
	/**用量4*/
	@Excel(name = "用量4", width = 15)
    @ApiModelProperty(value = "用量4")
    private java.lang.Double u4;
	/**原材料5*/
	@Excel(name = "原材料5", width = 15)
    @ApiModelProperty(value = "原材料5")
    private java.lang.String m5;
	/**用量5*/
	@Excel(name = "用量5", width = 15)
    @ApiModelProperty(value = "用量5")
    private java.lang.Double u5;
	/**原材料6*/
	@Excel(name = "原材料6", width = 15)
    @ApiModelProperty(value = "原材料6")
    private java.lang.String m6;
	/**用量6*/
	@Excel(name = "用量6", width = 15)
    @ApiModelProperty(value = "用量6")
    private java.lang.Double u6;
	/**原材料7*/
	@Excel(name = "原材料7", width = 15)
    @ApiModelProperty(value = "原材料7")
    private java.lang.String m7;
	/**用量7*/
	@Excel(name = "用量7", width = 15)
    @ApiModelProperty(value = "用量7")
    private java.lang.Double u7;
	/**原材料8*/
	@Excel(name = "原材料8", width = 15)
    @ApiModelProperty(value = "原材料8")
    private java.lang.String m8;
	/**用量8*/
	@Excel(name = "用量8", width = 15)
    @ApiModelProperty(value = "用量8")
    private java.lang.Double u8;
	/**原材料9*/
	@Excel(name = "原材料9", width = 15)
    @ApiModelProperty(value = "原材料9")
    private java.lang.String m9;
	/**用量9*/
	@Excel(name = "用量9", width = 15)
    @ApiModelProperty(value = "用量9")
    private java.lang.Double u9;
	/**原材料10*/
	@Excel(name = "原材料10", width = 15)
    @ApiModelProperty(value = "原材料10")
    private java.lang.String m10;
	/**用量10*/
	@Excel(name = "用量10", width = 15)
    @ApiModelProperty(value = "用量10")
    private java.lang.Double u10;
	/**原材料11*/
	@Excel(name = "原材料11", width = 15)
    @ApiModelProperty(value = "原材料11")
    private java.lang.String m11;
	/**用量11*/
	@Excel(name = "用量11", width = 15)
    @ApiModelProperty(value = "用量11")
    private java.lang.Double u11;
	/**原材料12*/
	@Excel(name = "原材料12", width = 15)
    @ApiModelProperty(value = "原材料12")
    private java.lang.String m12;
	/**用量12*/
	@Excel(name = "用量12", width = 15)
    @ApiModelProperty(value = "用量12")
    private java.lang.Double u12;
	/**原材料13*/
	@Excel(name = "原材料13", width = 15)
    @ApiModelProperty(value = "原材料13")
    private java.lang.String m13;
	/**用量13*/
	@Excel(name = "用量13", width = 15)
    @ApiModelProperty(value = "用量13")
    private java.lang.Double u13;
	/**原材料14*/
	@Excel(name = "原材料14", width = 15)
    @ApiModelProperty(value = "原材料14")
    private java.lang.String m14;
	/**用量14*/
	@Excel(name = "用量14", width = 15)
    @ApiModelProperty(value = "用量14")
    private java.lang.Double u14;
	/**原材料15*/
	@Excel(name = "原材料15", width = 15)
    @ApiModelProperty(value = "原材料15")
    private java.lang.String m15;
	/**用量15*/
	@Excel(name = "用量15", width = 15)
    @ApiModelProperty(value = "用量15")
    private java.lang.Double u15;
	/**原材料16*/
	@Excel(name = "原材料16", width = 15)
    @ApiModelProperty(value = "原材料16")
    private java.lang.String m16;
	/**用量16*/
	@Excel(name = "用量16", width = 15)
    @ApiModelProperty(value = "用量16")
    private java.lang.Double u16;
	/**原材料17*/
	@Excel(name = "原材料17", width = 15)
    @ApiModelProperty(value = "原材料17")
    private java.lang.String m17;
	/**用量17*/
	@Excel(name = "用量17", width = 15)
    @ApiModelProperty(value = "用量17")
    private java.lang.Double u17;
	/**原材料18*/
	@Excel(name = "原材料18", width = 15)
    @ApiModelProperty(value = "原材料18")
    private java.lang.String m18;
	/**用量18*/
	@Excel(name = "用量18", width = 15)
    @ApiModelProperty(value = "用量18")
    private java.lang.Double u18;
	/**原材料19*/
	@Excel(name = "原材料19", width = 15)
    @ApiModelProperty(value = "原材料19")
    private java.lang.String m19;
	/**用量19*/
	@Excel(name = "用量19", width = 15)
    @ApiModelProperty(value = "用量19")
    private java.lang.Double u19;
	/**原材料20*/
	@Excel(name = "原材料20", width = 15)
    @ApiModelProperty(value = "原材料20")
    private java.lang.String m20;
	/**用量20*/
	@Excel(name = "用量20", width = 15)
    @ApiModelProperty(value = "用量20")
    private java.lang.Double u20;
	/**原材料21*/
	@Excel(name = "原材料21", width = 15)
    @ApiModelProperty(value = "原材料21")
    private java.lang.String m21;
	/**用量21*/
	@Excel(name = "用量21", width = 15)
    @ApiModelProperty(value = "用量21")
    private java.lang.Double u21;
	/**原材料22*/
	@Excel(name = "原材料22", width = 15)
    @ApiModelProperty(value = "原材料22")
    private java.lang.String m22;
	/**用量22*/
	@Excel(name = "用量22", width = 15)
    @ApiModelProperty(value = "用量22")
    private java.lang.Double u22;
	/**原材料23*/
	@Excel(name = "原材料23", width = 15)
    @ApiModelProperty(value = "原材料23")
    private java.lang.String m23;
	/**用量23*/
	@Excel(name = "用量23", width = 15)
    @ApiModelProperty(value = "用量23")
    private java.lang.Double u23;
	/**原材料24*/
	@Excel(name = "原材料24", width = 15)
    @ApiModelProperty(value = "原材料24")
    private java.lang.String m24;
	/**用量24*/
	@Excel(name = "用量24", width = 15)
    @ApiModelProperty(value = "用量24")
    private java.lang.Double u24;
	/**原材料25*/
	@Excel(name = "原材料25", width = 15)
    @ApiModelProperty(value = "原材料25")
    private java.lang.String m25;
	/**用量25*/
	@Excel(name = "用量25", width = 15)
    @ApiModelProperty(value = "用量25")
    private java.lang.Double u25;
	/**原材料26*/
	@Excel(name = "原材料26", width = 15)
    @ApiModelProperty(value = "原材料26")
    private java.lang.String m26;
	/**用量26*/
	@Excel(name = "用量26", width = 15)
    @ApiModelProperty(value = "用量26")
    private java.lang.Double u26;
	/**原材料27*/
	@Excel(name = "原材料27", width = 15)
    @ApiModelProperty(value = "原材料27")
    private java.lang.String m27;
	/**用量27*/
	@Excel(name = "用量27", width = 15)
    @ApiModelProperty(value = "用量27")
    private java.lang.Double u27;
	/**原材料28*/
	@Excel(name = "原材料28", width = 15)
    @ApiModelProperty(value = "原材料28")
    private java.lang.String m28;
	/**用量28*/
	@Excel(name = "用量28", width = 15)
    @ApiModelProperty(value = "用量28")
    private java.lang.Double u28;
	/**原材料29*/
	@Excel(name = "原材料29", width = 15)
    @ApiModelProperty(value = "原材料29")
    private java.lang.String m29;
	/**用量29*/
	@Excel(name = "用量29", width = 15)
    @ApiModelProperty(value = "用量29")
    private java.lang.Double u29;
	/**原材料30*/
	@Excel(name = "原材料30", width = 15)
    @ApiModelProperty(value = "原材料30")
    private java.lang.String m30;
	/**用量30*/
	@Excel(name = "用量30", width = 15)
    @ApiModelProperty(value = "用量30")
    private java.lang.Double u30;
	/**原材料31*/
	@Excel(name = "原材料31", width = 15)
    @ApiModelProperty(value = "原材料31")
    private java.lang.String m31;
	/**用量31*/
	@Excel(name = "用量31", width = 15)
    @ApiModelProperty(value = "用量31")
    private java.lang.Double u31;
	/**原材料32*/
	@Excel(name = "原材料32", width = 15)
    @ApiModelProperty(value = "原材料32")
    private java.lang.String m32;
	/**用量32*/
	@Excel(name = "用量32", width = 15)
    @ApiModelProperty(value = "用量32")
    private java.lang.Double u32;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**控制权限*/
    @ApiModelProperty(value = "控制权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**理论配合比编号*/
	@Excel(name = "理论配合比编号", width = 15)
    @ApiModelProperty(value = "理论配合比编号")
    private java.lang.String code1;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projname;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String conspos;
	/**浇筑方式*/
	@Excel(name = "浇筑方式", width = 15)
    @ApiModelProperty(value = "浇筑方式")
    private java.lang.String pour;
	/**含水率（原材料4骨料1）*/
	@Excel(name = "含水率（原材料4骨料1）", width = 15)
    @ApiModelProperty(value = "含水率（原材料4骨料1）")
    private java.lang.Double mc4;
	/**含水率（原材料5骨料2）*/
	@Excel(name = "含水率（原材料5骨料2）", width = 15)
    @ApiModelProperty(value = "含水率（原材料5骨料2）")
    private java.lang.Double mc5;
	/**含水率（原材料6骨料3）*/
	@Excel(name = "含水率（原材料6骨料3）", width = 15)
    @ApiModelProperty(value = "含水率（原材料6骨料3）")
    private java.lang.Double mc6;
	/**含水率（原材料7骨料4）*/
	@Excel(name = "含水率（原材料7骨料4）", width = 15)
    @ApiModelProperty(value = "含水率（原材料7骨料4）")
    private java.lang.Double mc7;
	/**含水率（原材料8外加剂1）*/
	@Excel(name = "含水率（原材料8外加剂1）", width = 15)
    @ApiModelProperty(value = "含水率（原材料8外加剂1）")
    private java.lang.Double mc8;
	/**含水率（原材料9外加剂2）*/
	@Excel(name = "含水率（原材料9外加剂2）", width = 15)
    @ApiModelProperty(value = "含水率（原材料9外加剂2）")
    private java.lang.Double mc9;
	/**含水率（原材料10外加剂3）*/
	@Excel(name = "含水率（原材料10外加剂3）", width = 15)
    @ApiModelProperty(value = "含水率（原材料10外加剂3）")
    private java.lang.Double mc10;
	/**实际用量（原材料4骨料1）*/
	@Excel(name = "实际用量（原材料4骨料1）", width = 15)
    @ApiModelProperty(value = "实际用量（原材料4骨料1）")
    private java.lang.Double ru4;
	/**实际用量（原材料5骨料2）*/
	@Excel(name = "实际用量（原材料5骨料2）", width = 15)
    @ApiModelProperty(value = "实际用量（原材料5骨料2）")
    private java.lang.Double ru5;
	/**实际用量（原材料6骨料3）*/
	@Excel(name = "实际用量（原材料6骨料3）", width = 15)
    @ApiModelProperty(value = "实际用量（原材料6骨料3）")
    private java.lang.Double ru6;
	/**实际用量（原材料7骨料4）*/
	@Excel(name = "实际用量（原材料7骨料4）", width = 15)
    @ApiModelProperty(value = "实际用量（原材料7骨料4）")
    private java.lang.Double ru7;
	/**实际用量（原材料8外加剂1）*/
	@Excel(name = "实际用量（原材料8外加剂1）", width = 15)
    @ApiModelProperty(value = "实际用量（原材料8外加剂1）")
    private java.lang.Double ru8;
	/**实际用量（原材料9外加剂2）*/
	@Excel(name = "实际用量（原材料9外加剂2）", width = 15)
    @ApiModelProperty(value = "实际用量（原材料9外加剂2）")
    private java.lang.Double ru9;
	/**实际用量（原材料10外加剂3）*/
	@Excel(name = "实际用量（原材料10外加剂3）", width = 15)
    @ApiModelProperty(value = "实际用量（原材料10外加剂3）")
    private java.lang.Double ru10;
	/**鉴定编号*/
	@Excel(name = "鉴定编号", width = 15)
    @ApiModelProperty(value = "鉴定编号")
    private java.lang.String identificationno;
	/**鉴定日期*/
	@Excel(name = "鉴定日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "鉴定日期")
    private java.util.Date identificationtime;
	/**鉴定设计塌落度(mm)*/
	@Excel(name = "鉴定设计塌落度(mm)", width = 15)
    @ApiModelProperty(value = "鉴定设计塌落度(mm)")
    private java.lang.String jdsjlands;
	/**鉴定实测塌落度1(mm)*/
	@Excel(name = "鉴定实测塌落度1(mm)", width = 15)
    @ApiModelProperty(value = "鉴定实测塌落度1(mm)")
    private java.lang.String jdsclands1;
	/**鉴定实测塌落度2(mm)*/
	@Excel(name = "鉴定实测塌落度2(mm)", width = 15)
    @ApiModelProperty(value = "鉴定实测塌落度2(mm)")
    private java.lang.String jdsclands2;
	/**鉴定实测塌落度3(mm)*/
	@Excel(name = "鉴定实测塌落度3(mm)", width = 15)
    @ApiModelProperty(value = "鉴定实测塌落度3(mm)")
    private java.lang.String jdsclands3;
	/**鉴定设计含水量（%）*/
	@Excel(name = "鉴定设计含水量（%）", width = 15)
    @ApiModelProperty(value = "鉴定设计含水量（%）")
    private java.lang.Double jdsjmc;
	/**鉴定实测含水量1（%）*/
	@Excel(name = "鉴定实测含水量1（%）", width = 15)
    @ApiModelProperty(value = "鉴定实测含水量1（%）")
    private java.lang.Double jdscmc1;
	/**鉴定实测含水量2（%）*/
	@Excel(name = "鉴定实测含水量2（%）", width = 15)
    @ApiModelProperty(value = "鉴定实测含水量2（%）")
    private java.lang.Double jdscmc2;
	/**鉴定实测含水量3（%）*/
	@Excel(name = "鉴定实测含水量3（%）", width = 15)
    @ApiModelProperty(value = "鉴定实测含水量3（%）")
    private java.lang.Double jdscmc3;
	/**鉴定设计出机温度（℃）*/
	@Excel(name = "鉴定设计出机温度（℃）", width = 15)
    @ApiModelProperty(value = "鉴定设计出机温度（℃）")
    private java.lang.Double jdsjcjwd;
	/**鉴定实测出机温度1（℃）*/
	@Excel(name = "鉴定实测出机温度1（℃）", width = 15)
    @ApiModelProperty(value = "鉴定实测出机温度1（℃）")
    private java.lang.Double jdsccjwd1;
	/**鉴定实测出机温度2（℃）*/
	@Excel(name = "鉴定实测出机温度2（℃）", width = 15)
    @ApiModelProperty(value = "鉴定实测出机温度2（℃）")
    private java.lang.Double jdsccjwd2;
	/**鉴定实测出机温度3（℃）*/
	@Excel(name = "鉴定实测出机温度3（℃）", width = 15)
    @ApiModelProperty(value = "鉴定实测出机温度3（℃）")
    private java.lang.Double jdsccjwd3;
    /**鉴定设计泌水率*/
    @Excel(name = "鉴定设计泌水率", width = 15)
    @ApiModelProperty(value = "鉴定设计泌水率1")
    private java.lang.Double jdsjmsl;
	/**鉴定实测泌水率1*/
	@Excel(name = "鉴定实测泌水率1", width = 15)
    @ApiModelProperty(value = "鉴定实测泌水率1")
    private java.lang.Double jdscmsl1;
	/**鉴定实测泌水率2*/
	@Excel(name = "鉴定实测泌水率2", width = 15)
    @ApiModelProperty(value = "鉴定实测泌水率2")
    private java.lang.Double jdscmsl2;
	/**鉴定实测泌水率3*/
	@Excel(name = "鉴定实测泌水率3", width = 15)
    @ApiModelProperty(value = "鉴定实测泌水率3")
    private java.lang.Double jdscmsl3;
	/**原材料与申请单是否相符*/
	@Excel(name = "原材料与申请单是否相符", width = 15)
    @Dict(dicCode = "ismatch")
    @ApiModelProperty(value = "原材料与申请单是否相符")
    private java.lang.Integer ismatch;
	/**鉴定结论*/
	@Excel(name = "鉴定结论", width = 15)
    @ApiModelProperty(value = "鉴定结论")
    private java.lang.String identificationresult;
	/**鉴定附件*/
	@Excel(name = "鉴定附件", width = 15)
    @ApiModelProperty(value = "鉴定附件")
    private java.lang.String filePath;
	/**试验员*/
	@Excel(name = "试验员", width = 15)
    @ApiModelProperty(value = "试验员")
    private java.lang.String shiyanyuan;
	/**技术主管*/
	@Excel(name = "技术主管", width = 15)
    @ApiModelProperty(value = "技术主管")
    private java.lang.String technicalDirector;
	/**监理工程师*/
	@Excel(name = "监理工程师", width = 15)
    @ApiModelProperty(value = "监理工程师")
    private java.lang.String supervisionEngineer;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String beizhu;
    /**审核状态*/
    @Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
    @Dict(dicCode = "hntspjdstatus")
    private java.lang.Integer status;//数据库默认是0  未审核
}
